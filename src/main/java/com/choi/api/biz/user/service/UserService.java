package com.choi.api.biz.user.service;

import com.choi.api.biz.mail.model.Mail;
import com.choi.api.biz.mail.model.MailDTO;
import com.choi.api.biz.mail.service.MailService;
import com.choi.api.core.exception.BizException;
import com.choi.api.core.exception.SystemException;
import com.choi.api.core.exception.UnauthorizedException;
import com.choi.api.core.model.ApiResponse;
import com.choi.api.core.model.ApiResponseData;
import com.choi.api.core.model.ApiResponseErr;
import com.choi.api.biz.user.dao.UserRepository;
import com.choi.api.biz.user.model.User;
import com.choi.api.biz.user.model.UserDTO;
import com.choi.api.biz.user.model.UserToken;
import com.choi.api.core.redis.service.RedisService;
import com.choi.api.core.security.service.JwtService;
import com.choi.api.core.util.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private MailService mailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisService redisService;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public ApiResponse save(UserDTO.UserJoinReq req){

        if(!StringUtils.hasLength(req.getUsername()) || !StringUtils.hasLength(req.getPassword()) || !StringUtils.hasLength(req.getEmail())){
            return new ApiResponse(ApiResponse.Status.fail, "필수 입력값을 확인해주세요. [아이디, 비밀번호, 이메일]");
        }

        String username = "";
        try {

            User duplChk = repository.findByUsername(req.getUsername());
            if(!Objects.isNull(duplChk)){
                return new ApiResponse(ApiResponse.Status.fail, "이미 존재하는 아이디입니다.");
            }
            duplChk = repository.findByEmail(req.getEmail());
            if(!Objects.isNull(duplChk)){
                return new ApiResponse(ApiResponse.Status.fail, "이미 가입한 이메일입니다.");
            }

                username  = repository.saveAndFlush(User.builder()
                        .id(String.valueOf(UUID.randomUUID()))
                        .username(req.getUsername())
                        .password(encoder.encode(req.getPassword()))
                        .email(req.getEmail())
                        .authority("user")
                        .status("Y")
                        .build()
                ).getUsername();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }

        return new ApiResponseData(ApiResponse.Status.success, username);
    }

    public ApiResponse verifyEmail(MailDTO.SendMailReq req){
        User chk = null;
        if(req.getType().equals("emailForJoin")){
            chk = repository.findByEmail(req.getEmail());
            if(!Objects.isNull(chk)){
                return new ApiResponse(ApiResponse.Status.fail, "이미 가입한 이메일입니다.");
            }
        } else {
            chk = repository.findByEmailAndUsername(req.getEmail(), req.getUsername());
            if(Objects.isNull(chk)){
                return new ApiResponse(ApiResponse.Status.fail, "가입 정보가 존재하지 않습니다.");
            }
        }

        String subject = req.getType().equals("emailForJoin") ? "[NiceDiary] 회원가입을 위한 인증메일" :  "[NiceDiary] 아이디/비밀번호 찾기를 위한 인증메일";
        Mail mail = Mail.builder()
                .email(req.getEmail())
                .subject(subject)
                .type(req.getType())
                .build();

        int seq = 0;
        try {
            seq = mailService.sendMail(mail);
            if(seq == 0){
                throw new BizException("인증 이메일 발송에 실패했습니다. 가입을 다시 진행해주세요.");
            }

        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }

        return new ApiResponseData<Integer>(ApiResponse.Status.success, seq);
    }

    public ApiResponse verifyEmailCode(MailDTO.VerifyCodeReq req){
        Mail mail = Mail.builder()
                .email(req.getEmail())
                .seq(req.getSeq())
                .code(req.getCode())
                .build();

        String code = "";
        try {
            code = mailService.verifyCode(mail);
            if(!StringUtils.hasLength(code)){
                return new ApiResponse(ApiResponse.Status.fail, "인증코드가 일치하지 않습니다.");
            } else if(!req.getCode().equals(code)){
                return new ApiResponse(ApiResponse.Status.fail, "인증코드가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }

        return new ApiResponse(ApiResponse.Status.success);
    }

    @Transactional
    public ApiResponse findPwd(UserDTO.FindPwdReq req){
        Mail mail = Mail.builder()
                .email(req.getEmail())
                .seq(req.getSeq())
                .code(req.getCode())
                .build();

        User user = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .build();

        String code = "";
        try {
            code = mailService.verifyCode(mail);
            if(!StringUtils.hasLength(code)){
                return new ApiResponse(ApiResponse.Status.fail, "인증코드가 일치하지 않습니다.");
            } else if(!req.getCode().equals(code)){
                return new ApiResponse(ApiResponse.Status.fail, "인증코드가 일치하지 않습니다.");
            }

            int res = repository.updateByUsername(user);
            if(res == 0){
                return new ApiResponse(ApiResponse.Status.fail, "계정 정보가 존재하지 않습니다.");
            } else {
                return new ApiResponse(ApiResponse.Status.success);
            }

        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }
    }

    @Transactional
    public ApiResponse chgPwd(UserDTO.ChgPwdReq req){
        String accessToken = jwtService.getAccessToken();
        String userId = jwtService.get(accessToken, "userId");

        Optional<User> user = Optional.ofNullable(repository.findById(userId)
                .orElseThrow(() -> new BizException("계정 정보가 일치하지 않습니다.")));

        try {

            if (!encoder.matches(req.getPrevPwd(), user.get().getPassword())) {
                return new ApiResponse(ApiResponse.Status.fail, "이전 비밀번호가 일치하지 않습니다.");
            }

            User param = User.builder()
                    .id(userId)
                    .password(encoder.encode(req.getNewPwd()))
                    .build();
            int res = repository.updateByUsernameAndId(param);
            if(res == 0){
                return new ApiResponse(ApiResponse.Status.fail, "계정 정보가 존재하지 않습니다.");
            } else {
                return new ApiResponse(ApiResponse.Status.success);
            }
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }
    }

    public ApiResponse getUser(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String accessToken = jwtService.getAccessToken();
        String userId = jwtService.get(accessToken, "userId");

        try {
            Optional<User> user = Optional.ofNullable(repository.findById(userId)
                    .orElseThrow(() -> new UnauthorizedException()));

            UserDTO.GetUserRes res = UserDTO.GetUserRes.builder()
                    .username(user.get().getUsername())
                    .email(user.get().getEmail())
                    .insdate(user.get().getSysCreationDate().format(formatter))
                    .build();

            return new ApiResponseData(ApiResponse.Status.success, res);
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }
    }

    public ApiResponse isLogin(){
        /*String authorizationHeader = RequestUtils.getRequestHeader("Authorization");
        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }

        String authrization = authorizationHeader.substring(7);
        UserToken userToken = objectMapper.convertValue(authrization, UserToken.class);

        Map<String, String> data = new HashMap<>();
        data.put("userToken", userToken.getKey());*/
        return new ApiResponse(ApiResponse.Status.success);
    }

    public ApiResponse refresh(UserDTO.RefreshReq req) {

        try {
            String username = req.getUsername();
            String bearerToken = jwtService.getAccessToken();
            User user = Optional.ofNullable(repository.findByUsername(username))
                    .orElseThrow(() -> new BizException("로그인 정보가 존재하지 않습니다."));

            // 리프레시 토큰 검증
            String refreshToken = redisService.getValue(user.getId() + "_refresh");
            if(!StringUtils.hasLength(refreshToken)){
                log.debug("refesh 디버그 : refresh 토큰 없음");
                throw new UnauthorizedException();
            }

            if(!bearerToken.equals(refreshToken)){
                log.debug("refesh 디버그 : refresh 토큰 불일치");
                throw new UnauthorizedException();
            }

            // 액세스토큰 재발급
            String renewalToken = jwtService.create(user.getId() + "_access", "userId", user.getId(), 1000 * 60 * 1);
            UserToken userToken = UserToken.builder()
                    .accessToken(renewalToken)
                    .refreshToken(refreshToken)
                    .username(user.getUsername())
                    .build();

            log.debug("refresh -> 토큰 재발급 : " + userToken.toString());
            return new ApiResponseData<UserToken>(ApiResponse.Status.success, userToken);

        } catch(BizException e) {
            throw new BizException(e.getMessage());
        } catch(Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> {}", e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }

    }

}

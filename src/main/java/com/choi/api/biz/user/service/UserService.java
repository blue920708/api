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
import com.choi.api.core.security.service.JwtService;
import com.choi.api.core.util.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

                username  = repository.save(User.builder()
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

        User duplChk = repository.findByEmail(req.getEmail());
        if(!Objects.isNull(duplChk)){
            return new ApiResponse(ApiResponse.Status.fail, "이미 가입한 이메일입니다.");
        }

        Mail mail = Mail.builder()
                .email(req.getEmail())
                .subject("[NiceDiary] 회원가입을 위한 인증메일")
                .type("emailForJoin")
                .build();

        String seq = "";
        try {
            seq = mailService.sendMail(mail);
            if(!StringUtils.hasLength(seq)){
                throw new BizException("인증 이메일 발송에 실패했습니다. 가입을 다시 진행해주세요.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            return new ApiResponseErr(e.getMessage(), "작업 처리중 오류가 발생하였습니다.");
        }

        return new ApiResponseData<String>(ApiResponse.Status.success, seq);
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
            throw new SystemException(e.getMessage());
        }

        return new ApiResponse(ApiResponse.Status.success);
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

    public ApiResponse refresh(String refresh, String username) {

        try {
            User user = repository.findByUsername(username);
            if(Objects.isNull(user)){
                throw new BizException("로그인 정보가 존재하지 않습니다.");
            }

            // 액세스토큰 재발급
            UserToken userToken = jwtService.refresh(refresh, user);

            // 유저토큰 재생성
            if(!Objects.isNull(userToken)){
                return new ApiResponseData<UserToken>(ApiResponse.Status.success, userToken);
            }
        } catch(BizException e) {
            throw new BizException(e.getMessage());
        } catch(Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> {}", e.getMessage());
            throw new SystemException();
        }

        return new ApiResponse(ApiResponse.Status.fail, "로그인 정보가 존재하지 않습니다.");
    }

}

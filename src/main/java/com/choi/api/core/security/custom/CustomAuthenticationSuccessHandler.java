package com.choi.api.core.security.custom;

import com.choi.api.core.model.ApiResponse;
import com.choi.api.core.model.ApiResponseData;
import com.choi.api.biz.user.model.UserToken;
import com.choi.api.core.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtService.create(user.getId(), "userId", String.valueOf(user.getId()), 1000 * 60 * 60);
        String refreshToken = jwtService.create("refresh", "userId", String.valueOf(user.getId()), 1000 * 60 * 60 * 60);
        UserToken userToken = UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .build();

        log.debug("CustomAuthenticationSuccessHandler 디버그 -> 유저아이디 {}", user.getId());
        log.debug("CustomAuthenticationSuccessHandler 디버그 -> 유저토큰 {}", userToken);

        response.setStatus(HttpServletResponse.SC_OK); // 200
        response.setContentType("application/json"); // json 타입
        ApiResponseData res = new ApiResponseData(ApiResponse.Status.success, userToken);
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}

package com.choi.api.core.aop;

import com.choi.api.core.exception.UnauthorizedException;
import com.choi.api.core.security.service.JwtService;
import com.choi.api.core.util.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Aspect
@Slf4j
public class CustomAspect {
    private static final String path = "com.choi.api";
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation( com.choi.api.core.annotation.UserAuth)")
    public Object userAuth(ProceedingJoinPoint joinPoint) throws Throwable {

        log.debug("userAuth : " + SecurityContextHolder.getContext().getAuthentication().getName());

        String authorizationHeader = RequestUtils.getRequestHeader("Authorization");
        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            log.debug(this.getClass().getName() + " 디버그 : 토큰 없음");
            throw new UnauthorizedException();
        }

        String accessToken = authorizationHeader.substring(7);
        if(StringUtils.isEmpty(accessToken)){
            log.debug(this.getClass().getName() + " 디버그 : 토큰 없음2");
            throw new UnauthorizedException();
        }

        if(jwtService.isTokenExpired(accessToken)){
            log.debug(this.getClass().getName() + " 디버그 : 토큰 만료");
            throw new UnauthorizedException("JWT_EXPIRED");
        }

        return joinPoint.proceed();
    }

}

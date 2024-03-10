package com.choi.api.core.aop;

import com.choi.api.core.exception.BizException;
import com.choi.api.core.exception.SystemException;
import com.choi.api.core.exception.UnauthorizedException;
import com.choi.api.core.model.ApiResponse;
import com.choi.api.core.model.ApiResponseErr;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class CustomAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleUnauthorizedException(HttpServletRequest req, UnauthorizedException e) {
        return new ApiResponseErr("AUTHORIZATION_ERROR", "로그인 정보가 존재하지 않습니다.");
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse handleBizException(HttpServletRequest req, BizException e) {
        return new ApiResponse(ApiResponse.Status.fail, e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleSystemException(HttpServletRequest req, SystemException e) {
        return new ApiResponseErr("SYSTEM_ERROR", "작업 처리 중 오류가 발생하였습니다.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleExpiredJwtException(ExpiredJwtException e) {
        return new ApiResponseErr("TOKEN_EXPIRED");
    }
}

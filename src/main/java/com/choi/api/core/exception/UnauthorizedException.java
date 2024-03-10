package com.choi.api.core.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException() {
        super("로그인 정보가 존재하지 않습니다.");
    }

    public UnauthorizedException(String message){
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

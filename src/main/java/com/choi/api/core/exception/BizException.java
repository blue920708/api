package com.choi.api.core.exception;

public class BizException extends RuntimeException{
    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
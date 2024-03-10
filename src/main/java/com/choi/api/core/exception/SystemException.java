package com.choi.api.core.exception;

public class SystemException extends RuntimeException{

    public SystemException() {super("작업 처리중 오류가 발생하였습니다.");}
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.example.exception;

import lombok.Getter;

@Getter
public class ResourceException extends RuntimeException{
    private final int code;
    private final String message;

    public ResourceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ResourceException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}

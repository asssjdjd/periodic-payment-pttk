package com.example.exception;

public class AccessDeniedException extends ResourceException {
    public AccessDeniedException(int code, String message) {
        super(code, message);
    }
    public AccessDeniedException(ExceptionCode exceptionCode) {
        super(exceptionCode.getCode(),exceptionCode.getMessage());
    }
//    public AccessDeniedException(ExceptionCode exceptionCode, Object[] args) {
//        super(exceptionCode.getCode(), exceptionCode.getMessage(), args);
//    }

    public AccessDeniedException() {
        super(ExceptionCode.ACCESS_DENIED);
    }
}

package com.example.periodic_payment.exception;

public class AuthenticationInvalidException extends ResourceException{
    public AuthenticationInvalidException(int code, String message) {
        super(code, message);
    }
    public AuthenticationInvalidException(ExceptionCode exceptionCode) {
        super(exceptionCode.getCode(), exceptionCode.getMessage());
    }

//    public AuthenticationInvalidException(ExceptionCode exceptionCode, Object[] args) {
//        super(exceptionCode.getCode(), exceptionCode.getMessage(), args);
//    }

    public AuthenticationInvalidException() {
        super(ExceptionCode.AUTH_INVALID_AUTHENTICATION.getCode(), "authentication.error400");
    }

    public AuthenticationInvalidException(String message) {
        super(ExceptionCode.AUTH_INVALID_AUTHENTICATION.getCode(), message);
    }
}

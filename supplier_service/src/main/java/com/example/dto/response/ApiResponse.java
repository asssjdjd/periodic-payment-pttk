package com.example.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse extends ResponseEntity<ApiResponse.Payload> {

    public ApiResponse(int code, String message) {
        super(new Payload(code, message, null), HttpStatus.OK);
    }

    public ApiResponse(int code, String message, Object data) {
        super(new Payload(code, message, data), HttpStatus.OK);
    }

    @Data
    @Value
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor(force = true)
    public static class Payload {
        private int code;
        private String message;
        private Object data;
    }
}
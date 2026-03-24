package com.example.periodic_payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * The class ApiResponse implements received result from server
 * @author LongPT
 * Link {@link com.example.periodic_payment}
 */

public class ErrorResponse extends ApiResponse{
    public ErrorResponse(int code, String message) {
        super(HttpStatus.BAD_REQUEST.value(), "Error");
    }

    public ErrorResponse(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public ErrorResponse(String message, Object data) {
        super(HttpStatus.BAD_REQUEST.value(), message, data);
    }

//    public ErrorResponse(String message, List<FieldError> errors) {
//        super(HttpStatus.BAD_REQUEST.value(), message, new ApiError(errors.stream().
//                map(e -> {
//                    Optional<String> code = Arrays.stream(e.getCodes()).filter(c -> Translator.toLocale(c) != c).findFirst();
//                    String strCode = code.isPresent() ? code.get() : e.getCodes()[0];
//                    return new Error(e.getField(), e.getRejectedValue(), Translator.toLocale(strCode, e.getArguments()));
//                }).collect(Collectors.toList())));
//    }

    @Value
    @AllArgsConstructor
    public static class ApiError {
        List<Error> errors;
    }

    @Value
    @AllArgsConstructor
    public static class Error {
        private String field;
        private Object value;
        private String message;
    }


}

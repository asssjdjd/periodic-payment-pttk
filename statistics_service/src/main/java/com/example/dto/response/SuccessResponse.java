package com.example.dto.response;

public class SuccessResponse extends ApiResponse{

    /**
     * Create a new {@code SuccessResponse}.
     */
    public SuccessResponse() {
        super(200,"success");
    }

    /**
     * Create a new {@code SuccessResponse} with the given data.
     * @param data
     */
    public SuccessResponse(Object data) {
        super(200, ("success"), data);
    }

    public SuccessResponse(Object data, String message) {
        super(200, message, data);
    }

}
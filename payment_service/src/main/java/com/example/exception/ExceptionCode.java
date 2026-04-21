package com.example.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    AUTH_INVALID_AUTHENTICATION(1002, "Lỗi xác thực người dùng"),
    AUTH_INVALID_TOKEN(401, "Token không hợp lệ hoặc đã hết hạn"),
    ACCESS_DENIED(403, "Bạn không có quyền truy cập tài nguyên này"),
    SYSTEM_BUSY(500, "Hệ thống đang bận, vui lòng thử lại sau"),
    CCCD_NOT_FOUND(405,"Không tìm thấy cccd!"),
    USER_NOT_FOUND(406,"Không tìm thấy user cần tìm"),
    DATA_NOT_FOUND(404,"Không tìm thấy dữ liệu yêu cầu"),
    LOAN_PAYMENT_SCHEDUE(407,"Hợp đồng bị lỗi"),
    INVALID_INPUT(407,"Đầu vào không hợp lệ"),
    LOAN_PAYMENT_SCHEDUE_NOT_FOUND(408, "Không tìm thấy schedule payment hợp lệ"),
    LOAN_PAYMENT_SCHEDUE_ALREADY_PAID(409,"Đã thay toán kỳ hạn này"),
    FIND_CONTRACT_BY_SCHEDULE_NOT_FOUND(410,"không tìm thấy hợp đồng ứng với kỳ thanh toán ");

    // some error code follow here

    private final int code;
    private final String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

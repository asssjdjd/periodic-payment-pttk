package com.example.periodic_payment.service;

public enum LoanPaymentScheduleState {
    PENDING,          // Chưa đến hạn
    PARTIALLY_PAID,   // Đã thanh toán một phần
    OVERDUE,          // Quá hạn
    PAID              // Đã tất toán kỳ
}

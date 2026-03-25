package com.example.service;

public enum LoanPaymentPriority {
    DUE_DATE, // Ngày đến hạn
    INTEREST_DUE,     // Lãi trong hạn
    PRINCIPLE_DUE,     // Gốc trong hạn

    PENALTY_DUE,      // Tổng số tiền phạt hiện đang nợ cho kỳ này.
    PENALTY_FEE,      // Phí phạt chậm do phát sinh
    OVERDUE_INTEREST, // Lãi quá hạn
    OVERDUE_PRINCIPLE, // Gốc quá hạn

    PRINCIPLE_PAID ,   // số tiền gốc khách đã trả
    INTEREST_PAID,     //  số tiền lãi thực tế đã trả
    PENALTY_FEE_PAID ,  // số tiền phạt thực tế đã trả
    OVERDUE_INTEREST_PAID, // số tiền lãi quá hạn thực tế đã trả
    OVERDUE_PRINCIPLE_PAID, // số tiền phạt trên gốc thực tế đã trả

}

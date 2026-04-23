package com.example.dto;

import java.math.BigDecimal;

public class EventPayloads {

    // 1. Payload khi thanh toán kỳ hạn thành công
    public record TransactionPaymentScheduleEvent(
            String scheduleId,
            String contractId,
            BigDecimal amountPaid,          // Tổng tiền khách nộp lần này

            // THÊM 4 TRƯỜNG NÀY ĐỂ ĐỒNG BỘ CHÍNH XÁC
            BigDecimal principlePaid,       // Tổng gốc đã trả tính đến hiện tại
            BigDecimal interestPaid,        // Tổng lãi đã trả tính đến hiện tại
            BigDecimal penaltyFeePaid,      // Tổng phạt đã trả tính đến hiện tại
            BigDecimal overdueInterestPaid, // Tổng lãi quá hạn đã trả tính đến hiện tại

            String scheduleStatus,
            String transactionId
    ) {}

    // 2. Payload khi hợp đồng hoàn tất
    public record UpdateContractStatusEvent(
            String contractId,
            String status
    ) {}

    // 3. Payload khi Job chạy cập nhật quá hạn
    public record OverdueScheduleEvent(
            String scheduleId,
            BigDecimal overdueInterest,
            BigDecimal penaltyFee,
            String status
    ) {}
}
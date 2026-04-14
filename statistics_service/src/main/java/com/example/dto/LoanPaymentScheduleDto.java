package com.example.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaymentScheduleDto {
    int termNo;
    LocalDateTime dueDate;
    BigDecimal penaltyFee;
    BigDecimal overdueInterest;
    BigDecimal interestDue;
    BigDecimal principalDue;
    BigDecimal penaltyDue;
    String status;
    BigDecimal penaltyFeePaid;
    BigDecimal overdueInterestPaid;
    BigDecimal interestPaid;
    BigDecimal principlePaid;
}

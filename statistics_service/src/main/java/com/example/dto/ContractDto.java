package com.example.dto;

import com.example.entity.LoanPaymentSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {

    private List<LoanPaymentScheduleDto> loanPaymentSchedules;
    private LocalDate signDate;
    private BigDecimal Debt;
    private BigDecimal InterestRemaining;
    private BigDecimal PrincipleRemaining;
    private BigDecimal Penalty;
    private BigDecimal Overdue;
}

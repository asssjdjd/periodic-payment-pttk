package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatisticDto {
    private List<ContractDto> contracts;
    private BigDecimal totalDebt;
    private BigDecimal totalInterestRemaining;
    private BigDecimal totalPrincipleRemaining;
    private BigDecimal totalPenalty;
    private BigDecimal totalOverdue;

    private String customerId;
    private String customerName;
    private String customerPhone;
}

package com.example.dto.response;


import com.example.dto.LoanPaymentScheduleDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaymentScheduleResponse {
    private String id;
    private Integer termNo;
    private LocalDateTime dueDate;
    private BigDecimal penaltyFee;
    private BigDecimal overdueInterest;
    private BigDecimal interestDue;
    private BigDecimal principalDue;
    private String status;
    private BigDecimal penaltyFeePaid;
    private BigDecimal overdueInterestPaid;
    private BigDecimal interestPaid;
    private BigDecimal principlePaid;


    public static LoanPaymentScheduleResponse mapFromLoanPaymentDto(LoanPaymentScheduleDTO loanPaymentScheduleDTO) {
        return LoanPaymentScheduleResponse.builder()
                .id(loanPaymentScheduleDTO.getId())
                .termNo(loanPaymentScheduleDTO.getTermNo())
                .dueDate(loanPaymentScheduleDTO.getDueDate())
                .penaltyFee(loanPaymentScheduleDTO.getPenaltyFee())
                .overdueInterest(loanPaymentScheduleDTO.getOverdueInterest())
                .interestDue(loanPaymentScheduleDTO.getInterestDue())
                .principalDue(loanPaymentScheduleDTO.getPrincipalDue())
                .status(loanPaymentScheduleDTO.getStatus())
                .penaltyFeePaid(loanPaymentScheduleDTO.getPenaltyFeePaid())
                .overdueInterestPaid(loanPaymentScheduleDTO.getOverdueInterestPaid())
                .interestPaid(loanPaymentScheduleDTO.getInterestPaid())
                .principlePaid(loanPaymentScheduleDTO.getPrinciplePaid())
                .build();
    }

}

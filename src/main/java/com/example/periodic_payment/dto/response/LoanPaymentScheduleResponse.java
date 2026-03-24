package com.example.periodic_payment.dto.response;

import com.example.periodic_payment.dto.LoanPaymentScheduleDTO;
import com.example.periodic_payment.model.Contract;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaymentScheduleResponse {
    private Long id;
    private Long scheduleId;
    private Integer termNo;
    private LocalDate dueDate;
    private BigDecimal penaltyFee;
    private BigDecimal overdueInterest;
    private BigDecimal overduePrinciple;
    private BigDecimal interestDue;
    private BigDecimal principalDue;
    private BigDecimal penaltyDue;
    private String status;
    private BigDecimal penaltyFeePaid;
    private BigDecimal overdueInterestPaid;
    private BigDecimal overduePrinciplePaid;
    private BigDecimal interestPaid;
    private BigDecimal principlePaid;
    private Long contractId;
    private String contractCode;
    private BigDecimal overdueInterestRate;
    private BigDecimal overduePrincipleRate;

    public static LoanPaymentScheduleResponse fromEntity(LoanPaymentScheduleDTO loanPaymentSchedule) {
        return LoanPaymentScheduleResponse.builder()
                .id(loanPaymentSchedule.getId())
                // get contract_ID
                .contractId(loanPaymentSchedule.getContractId())
                // get contract_code
                .contractCode(loanPaymentSchedule.getContractCode())
                .scheduleId(loanPaymentSchedule.getScheduleId())
                .termNo(loanPaymentSchedule.getTermNo())
                .dueDate(loanPaymentSchedule.getDueDate())
                .penaltyFee(loanPaymentSchedule.getPenaltyFee())
                .overdueInterest(loanPaymentSchedule.getOverdueInterest())
                .overduePrinciple(loanPaymentSchedule.getOverduePrinciple())
                .interestDue(loanPaymentSchedule.getInterestDue())
                .principalDue(loanPaymentSchedule.getPrincipalDue())
                .penaltyDue(loanPaymentSchedule.getPenaltyDue())
                .status(loanPaymentSchedule.getStatus())
                .penaltyFeePaid(loanPaymentSchedule.getPenaltyFeePaid())
                .overdueInterestPaid(loanPaymentSchedule.getOverdueInterestPaid())
                .overduePrinciplePaid(loanPaymentSchedule.getOverduePrinciplePaid())
                .interestPaid(loanPaymentSchedule.getInterestPaid())
                .principlePaid(loanPaymentSchedule.getPrinciplePaid())
                // get overdueInterestRate
                .overdueInterestRate(loanPaymentSchedule.getOverdueInterestRate())
                .overduePrincipleRate(loanPaymentSchedule.getOverduePrincipleRate())
                .build();
    }

}

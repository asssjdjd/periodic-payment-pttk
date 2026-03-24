package com.example.periodic_payment.dto;

import com.example.periodic_payment.dto.response.ContractResponse;
import com.example.periodic_payment.model.LoanPaymentSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class LoanPaymentScheduleDTO {
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
    private BigDecimal interestRate;

    public static LoanPaymentScheduleDTO fromEntity(LoanPaymentSchedule loanPaymentSchedule) {
        return LoanPaymentScheduleDTO.builder()
                .id(loanPaymentSchedule.getId())
                // get contract_ID
                .contractId(loanPaymentSchedule.getContract().getId())
                // get contract_code
                .contractCode(loanPaymentSchedule.getContract().getCode())
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
                .overdueInterestRate(loanPaymentSchedule.getContract().getLoanOffer().getOverdueInterestRate())
                .overduePrincipleRate(loanPaymentSchedule.getContract().getLoanOffer().getOverduePrincipleRate())
                .interestRate(loanPaymentSchedule.getContract().getLoanOffer().getInterestRate())
                .build();
    }


}


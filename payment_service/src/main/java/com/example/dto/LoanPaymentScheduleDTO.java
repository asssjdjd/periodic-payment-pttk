package com.example.dto;

import com.example.model.LoanPaymentSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class LoanPaymentScheduleDTO {
    String id;
    int termNo;
    String contractId;
    LocalDateTime dueDate;
    BigDecimal penaltyFee;
    BigDecimal overdueInterest;
    BigDecimal interestDue;
    BigDecimal principalDue;
//    BigDecimal penaltyDue;
    String status;
    BigDecimal penaltyFeePaid;
    BigDecimal overdueInterestPaid;
    BigDecimal interestPaid;
    BigDecimal principlePaid;

    BigDecimal principleDueRate;
    BigDecimal interestDueRate;
    BigDecimal overdueInterestRate;


    public static LoanPaymentScheduleDTO mapLoanPaymentScheduleDto(LoanPaymentSchedule loanPaymentSchedule, BigDecimal principleDueRate, BigDecimal interestDueRate,BigDecimal overdueInterestRate) {
        return LoanPaymentScheduleDTO.builder()
                .id(loanPaymentSchedule.getScheduleId())
                .contractId(loanPaymentSchedule.getContractId())
                .termNo(loanPaymentSchedule.getTermNo())
                .dueDate(loanPaymentSchedule.getDueDate())
                .interestDue(loanPaymentSchedule.getInterestDue())
                .interestPaid(loanPaymentSchedule.getInterestPaid())
                .overdueInterest(loanPaymentSchedule.getOverdueInterest())
                .overdueInterestPaid(loanPaymentSchedule.getOverdueInterestPaid())
                .penaltyFee(loanPaymentSchedule.getPenaltyFee())
                .penaltyFeePaid(loanPaymentSchedule.getPenaltyFeePaid())
                .principalDue(loanPaymentSchedule.getPrincipalDue())
                .principlePaid(loanPaymentSchedule.getPrinciplePaid())
                .status(loanPaymentSchedule.getStatus())
                .principleDueRate(principleDueRate)
                .interestDueRate(interestDueRate)
                .overdueInterestRate(overdueInterestRate)
                .build();
    }

    public static LoanPaymentScheduleDTO mapLoanPaymentScheduleDtoNotCalculate(LoanPaymentSchedule loanPaymentSchedule) {
        return LoanPaymentScheduleDTO.builder()
                .id(loanPaymentSchedule.getScheduleId())
                .contractId(loanPaymentSchedule.getContractId())
                .termNo(loanPaymentSchedule.getTermNo())
                .dueDate(loanPaymentSchedule.getDueDate())
                .interestDue(loanPaymentSchedule.getInterestDue())
                .interestPaid(loanPaymentSchedule.getInterestPaid())
                .overdueInterest(loanPaymentSchedule.getOverdueInterest())
                .overdueInterestPaid(loanPaymentSchedule.getOverdueInterestPaid())
                .penaltyFee(loanPaymentSchedule.getPenaltyFee())
                .penaltyFeePaid(loanPaymentSchedule.getPenaltyFeePaid())
                .principalDue(loanPaymentSchedule.getPrincipalDue())
                .principlePaid(loanPaymentSchedule.getPrinciplePaid())
                .status(loanPaymentSchedule.getStatus())
                .build();
    }
}


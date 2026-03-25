package com.example.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transactionloanpaymentschedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLoanPaymentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanTransactionId", nullable = false)
    private LoanTransaction loanTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanPaymentScheduleId", nullable = false)
    private LoanPaymentSchedule loanPaymentSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", nullable = false)
    private Contract contract;
}

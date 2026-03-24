package com.example.periodic_payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loanpaymentschedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanPaymentSchedule extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", nullable = false)
    private Contract contract;

    @Column(name = "scheduleId") private Long scheduleId;
    @Column(name = "termNo", nullable = false) private Integer termNo;
    @Column(name = "dueDate", nullable = false) private LocalDate dueDate;

    @Column(name = "penaltyFee", nullable = false) private BigDecimal penaltyFee;
    @Column(name = "overdueInterest", nullable = false) private BigDecimal overdueInterest;
    @Column(name = "overduePrinciple", nullable = false) private BigDecimal overduePrinciple;
    @Column(name = "interestDue", nullable = false) private BigDecimal interestDue;
    @Column(name = "principalDue", nullable = false) private BigDecimal principalDue;
    @Column(name = "penaltyDue", nullable = false) private BigDecimal penaltyDue;
    @Column(name = "status") private String status;

    @Column(name = "penaltyFeePaid", nullable = false) private BigDecimal penaltyFeePaid;
    @Column(name = "overdueInterestPaid", nullable = false) private BigDecimal overdueInterestPaid;
    @Column(name = "overduePrinciplePaid", nullable = false) private BigDecimal overduePrinciplePaid;
    @Column(name = "interestPaid", nullable = false) private BigDecimal interestPaid;
    @Column(name = "principlePaid", nullable = false) private BigDecimal principlePaid;
}

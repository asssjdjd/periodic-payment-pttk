package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loanpaymentschedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanPaymentSchedule {

    @Id
    @Column(name = "scheduleId")
    String scheduleId;

    @Column(name = "contractId", nullable = false)
    String contractId;

    @Column(name = "termNo", nullable = false)
    int termNo;

    @Column(name = "dueDate", nullable = false)
    LocalDateTime dueDate;

    // --- CÁC KHOẢN PHẢI THU (DUE) ---

    @Column(name = "penaltyFee", precision = 18, scale = 2)
    BigDecimal penaltyFee;

    @Column(name = "overdueInterest", precision = 18, scale = 2)
    BigDecimal overdueInterest;

    @Column(name = "interestDue", precision = 18, scale = 2)
    BigDecimal interestDue;

    @Column(name = "principalDue", precision = 18, scale = 2)
    BigDecimal principalDue;

    @Column(name = "penaltyDue", precision = 18, scale = 2)
    BigDecimal penaltyDue;

    @Column(name = "status", length = 50)
    String status;

    // --- CÁC KHOẢN ĐÃ THANH TOÁN (PAID) ---

    @Column(name = "penaltyFeePaid", precision = 18, scale = 2)
    BigDecimal penaltyFeePaid;

    @Column(name = "overdueInterestPaid", precision = 18, scale = 2)
    BigDecimal overdueInterestPaid;

    @Column(name = "interestPaid", precision = 18, scale = 2)
    BigDecimal interestPaid;

    @Column(name = "principlePaid", precision = 18, scale = 2)
    BigDecimal principlePaid;

    // --- AUDITING FIELDS ---

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    LocalDateTime deletedAt;
}
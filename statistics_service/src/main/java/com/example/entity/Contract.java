package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract {
    @Id
    String id;

    @Column(name = "code", unique = true)
    String code;

    @Column(name = "userId", nullable = false)
    String userId;

    @Column(name = "customerId", nullable = false)
    String customerId;

    @Column(name = "signedDate", nullable = false)
    LocalDateTime signedDate;

    @Column(name = "productPrice", nullable = false)
    BigDecimal productPrice;

    @Column(name = "prepaidAmount", nullable = false)
    BigDecimal prepaidAmount;

    @Column(name = "loanAmount", nullable = false)
    BigDecimal loanAmount;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "loan_schedule_term_number")
    int loanScheduleTermNumber;

    @Column(name = "penalty_fee")
    BigDecimal penaltyFee;

    @Column(name = "principal_due_rate")
    BigDecimal principalDueRate;

    @Column(name = "interest_due_rate")
    BigDecimal interestDueRate;

    @Column(name = "overdue_interest_rate")
    BigDecimal overdueInterestRate;

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    LocalDateTime deletedAt; // Dùng cho cơ chế Soft Delete
}


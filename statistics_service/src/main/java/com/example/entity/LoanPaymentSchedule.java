package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_payment_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanPaymentSchedule extends BaseEntity {

    @Id
    @Column(name = "id")
    String scheduleId;

    @Column(name = "contract_id", nullable = false)
    String contractId;

    @Column(name = "term_no", nullable = false)
    int termNo;

    @Column(name = "due_date", nullable = false)
    LocalDate dueDate;

    // --- CÁC KHOẢN PHẢI THU (DUE) ---

    @Column(name = "penalty_fee", precision = 18, scale = 2)
    BigDecimal penaltyFee;

    @Column(name = "interest_overdue", precision = 18, scale = 2)
    BigDecimal overdueInterest;

    @Column(name = "interest_due", precision = 18, scale = 2)
    BigDecimal interestDue;

    @Column(name = "principal_due", precision = 18, scale = 2)
    BigDecimal principalDue;

    @Column(name = "status", length = 50)
    String status;

    // OVERDUE , PAID , PENDING

    // --- CÁC KHOẢN ĐÃ THANH TOÁN (PAID) ---

    @Column(name = "penalty_fee_paid", precision = 18, scale = 2)
    BigDecimal penaltyFeePaid;

    @Column(name = "interest_overdue_paid", precision = 18, scale = 2)
    BigDecimal overdueInterestPaid;

    @Column(name = "interest_paid", precision = 18, scale = 2)
    BigDecimal interestPaid;

    @Column(name = "principal_paid", precision = 18, scale = 2)
    BigDecimal principlePaid;

}
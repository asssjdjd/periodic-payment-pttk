package com.example.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction_loan_payment_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLoanPaymentSchedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "money_price", nullable = false)
    private BigDecimal amount;

    @Column(name = "loan_payment_schedule_id", nullable = false)
    private String scheduleId;

    @Column(name = "customer_id", nullable = false) // ĐÃ BỔ SUNG
    private String customerId;

    @Column(name = "user_id", nullable = false) // ĐÃ BỔ SUNG
    private String userId;

    @Column(name = "payment_date", nullable = false) // ĐÃ BỔ SUNG
    private LocalDate paymentDate;
}

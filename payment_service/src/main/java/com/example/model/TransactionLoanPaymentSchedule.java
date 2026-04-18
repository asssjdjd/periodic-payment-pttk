package com.example.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactionloanpaymentschedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLoanPaymentSchedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "amount", nullable = false)
    private BigDecimal amount;

    @JoinColumn(name = "scheduleId", nullable = false)
    private String scheduleId;
}

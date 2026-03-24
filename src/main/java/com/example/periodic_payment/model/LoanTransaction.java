package com.example.periodic_payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loantransaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTransaction extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transactionDate", nullable = false) private LocalDateTime transactionDate;
    @Column(name = "amountPaid", nullable = false) private BigDecimal amountPaid;
    @Column(name = "paymentMethod", nullable = false, length = 100) private String paymentMethod;
    @Column(columnDefinition = "TEXT") private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}

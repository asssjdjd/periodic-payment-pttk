package com.example.periodic_payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loanoffer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanOffer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(name = "name")
    private String name;

    @Column(name = "interestRate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "penaltyRate", nullable = false)
    private BigDecimal penaltyRate;

    @Column(name = "overdueInterestRate", nullable = false)
    private BigDecimal overdueInterestRate;

    @Column(name = "overduePrincipleRate", nullable = false)
    private BigDecimal overduePrincipleRate;

    @Column(name = "maxAmount", nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "termMonths", nullable = false)
    private Integer termMonths;
}

package com.example.periodic_payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "collateral")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collateral {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) private String type;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "valuationValue", nullable = false) private BigDecimal valuationValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractId", nullable = false)
    private Contract contract;
}

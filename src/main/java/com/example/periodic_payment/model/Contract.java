package com.example.periodic_payment.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    User user;

    // Map với bảng LoanOffer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanProductsId", nullable = false)
    LoanOffer loanOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanContractId")
    private Contract parentContract;

    @Column(name = "loanAmount", nullable = false)
    BigDecimal loanAmount;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "signedDate", nullable = false)
    private LocalDate signedDate;

    @Column(name = "productPrice", nullable = false)
    private BigDecimal productPrice;

    @Column(name = "prepaidAmount", nullable = false)
    private BigDecimal prepaidAmount;

    // Quan hệ 1-N với Tài sản thế chấp
    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    List<Collateral> collaterals;

    // Quan hệ 1-N với Contract
    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LoanPaymentSchedule> paymentSchedules;
}

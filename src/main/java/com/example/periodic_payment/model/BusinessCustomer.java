package com.example.periodic_payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BusinessCustomer")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class BusinessCustomer extends Customer{
//    @Column(name = "taxCode")
//    private String taxCode;
//
//    @Column(name = "companyName")
//    private String companyName;
}

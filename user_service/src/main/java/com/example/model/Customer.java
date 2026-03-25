package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Customer") // Khớp với bảng trong Liquibase
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "fullName", nullable = false)
    String fullName;

    @Column(name = "phoneNumber", nullable = false, length = 20)
    String phoneNumber;

    @Column(name = "creditScore")
    Integer creditScore;

    @Column(name = "status", nullable = false, length = 50)
    String status;

    @Column(name = "cccd")
    String cccd;
}

package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IndividualCustomer")
@PrimaryKeyJoinColumn(name = "id") // Nối với ID của Customer
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualCustomer extends Customer{
    @Column(name = "cccd", nullable = false, unique = true) private String individualCccd; // Đổi tên biến để tránh trùng lặp biến ở class cha
    @Column(nullable = false) private java.time.LocalDate dob;
}

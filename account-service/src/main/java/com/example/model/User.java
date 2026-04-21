package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users") // Khớp với bảng trong Liquibase
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
//
    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "email", nullable = false, length = 20)
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "country", nullable = false, length = 50)
    String country;

    @Column(name = "role", nullable = false, length = 50)
    String role;

}

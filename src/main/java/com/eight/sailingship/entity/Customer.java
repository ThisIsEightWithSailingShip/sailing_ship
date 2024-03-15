package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "customers")
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private RoleEnum role;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "account")
    private Integer account;
}

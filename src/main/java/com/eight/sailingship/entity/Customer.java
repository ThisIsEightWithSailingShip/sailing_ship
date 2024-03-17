package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
   // @Enumerated(EnumType.STRING) // 문자열로 열거형 상수를 저장
    private String role;


    @Column(name = "nickname")
    private String nickname;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "account")
    private Integer account;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Order> orderList;

    public void addOrderList(Order order) {
        this.orderList.add(order);
        order.setCustomer(this);
    }
}

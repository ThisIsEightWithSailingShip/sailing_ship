package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
//    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "account")
    private Integer account;

//    @Column(name = "is_owner", nullable = true)
//    private Boolean isOwner;
//
    @OneToOne
    @JoinColumn(name = "store_id", unique = true)
    private Store store;

   @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
     private List<Order> orderList;

    public void addOrderList(Order order) {
        this.orderList.add(order);
        order.setUser(this);
    }

}


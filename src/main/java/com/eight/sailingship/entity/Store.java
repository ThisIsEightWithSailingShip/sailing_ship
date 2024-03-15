package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "stores")
@Getter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private long storeId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private RoleEnum role;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "category")
    private StoreEnum category;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "owner_name")
    private String ownerName;
}

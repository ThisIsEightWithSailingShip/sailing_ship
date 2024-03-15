package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "stores")
@Getter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long storeId;

    private String email;

    private String password;

    private RoleEnum roleEnum;

    private String address;

    private String phone;

    private StoreEnum storeEnum;

    private String storeName;

    private String ownerName;
}

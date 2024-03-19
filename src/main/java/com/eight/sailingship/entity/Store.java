package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private long storeId;

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

    @OneToOne(mappedBy = "store")
    private Customer owner;

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Menu> menuList;


    public void addMenuList(Menu menu) {
        this.menuList.add(menu);
        menu.setStore(this);
    }
}

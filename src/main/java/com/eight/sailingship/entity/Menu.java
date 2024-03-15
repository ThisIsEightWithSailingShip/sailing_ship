package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    private String menuName;

    private String introduce;

    private Long price;

    private String menuCategory;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;
}

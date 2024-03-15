package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "ordermenus")
@Getter
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderMenuId;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
}

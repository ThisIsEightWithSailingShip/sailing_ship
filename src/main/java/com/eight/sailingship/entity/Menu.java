package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "price")
    private Long price;

    @Column(name = "menu_category")
    private String menuCategory;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderMenu> orderMenuList;

    public void addOrderMenuList(OrderMenu orderMenu) {
        this.orderMenuList.add(orderMenu);
        orderMenu.setMenu(this);
    }
}

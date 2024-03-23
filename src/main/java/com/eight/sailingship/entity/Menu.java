package com.eight.sailingship.entity;

import com.eight.sailingship.dto.menu.MenuRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
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

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderMenu> orderMenuList;

    @OneToOne(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private ImagePhoto imagePhoto;

    public Menu(MenuRequestDto requestDto, Store store, String url) {
        this.menuName = requestDto.getMenuName();
        this.introduce = requestDto.getIntroduce();
        this.price = requestDto.getPrice();
        this.menuCategory = requestDto.getMenuCategory();
        this.store = store;
        this.imageUrl = url;
    }

    public void update(MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.introduce = requestDto.getIntroduce();
        this.price = requestDto.getPrice();
        this.menuCategory = requestDto.getMenuCategory();
    }
}

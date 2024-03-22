package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "image")
public class ImagePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="store_storeId")
    private Store store;

    @OneToOne
    @JoinColumn(name = "menu_menuId")
    private Menu menu;

}

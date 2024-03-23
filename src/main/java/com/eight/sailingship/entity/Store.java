package com.eight.sailingship.entity;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.store.StoreRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@Setter
@RequiredArgsConstructor
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

    @OneToOne(cascade = CascadeType.ALL) // cascade를 하는 것이 맞는지
    @JoinColumn(name = "owner_id", unique = true)
    private User owner;

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Menu> menuList;

    @OneToMany(mappedBy = "store")
    private List<ImagePhoto> imagePhotos;

    @OneToOne(mappedBy = "store")
    private ImageStore imageStore;

    public Store(StoreRequestDto requestDto, UserDetailsImpl userDetails) {
        this.address = requestDto.getAddress();
        this.phone = requestDto.getPhone();
        this.category = StoreEnum.valueOf(requestDto.getCategory().toUpperCase());
        this.storeName = requestDto.getStoreName();
        this.ownerName = requestDto.getOwnerName();
        this.owner = userDetails.getUser();
    }


    public void addMenuList(Menu menu) {
        this.menuList.add(menu);
        menu.setStore(this);
    }

}

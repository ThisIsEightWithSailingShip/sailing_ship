package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "storeimage")
public class ImageStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column
    private String imageUrl;

    @OneToOne
    @JoinColumn(name="store_storeId")
    private Store store;

    public ImageStore(String storedFileName, Store store) {
        this.imageUrl = storedFileName;
        this.store = store;
    }
}

package com.eight.sailingship.dto.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequestDto {
    private String address;
    private String phone;
    private String category;
    private String storeName;
    private String ownerName;
}

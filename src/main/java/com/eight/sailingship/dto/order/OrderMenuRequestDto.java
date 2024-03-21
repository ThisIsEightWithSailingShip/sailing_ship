package com.eight.sailingship.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderMenuRequestDto {
    private Long menuId;
    private int quantity;
}

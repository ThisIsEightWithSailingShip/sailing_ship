package com.eight.sailingship.dto.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderMenuRequestDto {
    private Long menuId;
    private int quantity;
}

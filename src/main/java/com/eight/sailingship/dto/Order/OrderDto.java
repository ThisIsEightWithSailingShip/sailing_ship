package com.eight.sailingship.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private List<OrderMenuDto> menus;
    private int totalPrice;

    // Getter 및 Setter 메서드
}

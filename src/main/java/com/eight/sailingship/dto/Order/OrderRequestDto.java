package com.eight.sailingship.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private List<OrderMenuRequestDto> menus;
    private Long totalPrice;

    // Getter 및 Setter 메서드
}

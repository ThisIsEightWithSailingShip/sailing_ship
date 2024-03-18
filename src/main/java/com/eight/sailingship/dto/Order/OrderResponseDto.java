package com.eight.sailingship.dto.Order;

import com.eight.sailingship.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime orderDateTime;
    private List<OrderMenuResponseDto> orderMenuList;
    private Long totalPrice;
    private int status;
    public OrderResponseDto(List<OrderMenuResponseDto> orderMenus, Order customerOrder) {
        this.orderId = customerOrder.getOrderId();
        this.orderDateTime = customerOrder.getOrderDate();
        this.orderMenuList = orderMenus;
        this.totalPrice = customerOrder.getTotalPrice();
        this.status = customerOrder.getStatus();
    }
}

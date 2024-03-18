package com.eight.sailingship.service.order;

import com.eight.sailingship.dto.Order.OrderRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;

import java.util.List;

public interface OrderService {
    void save(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrderList();

}

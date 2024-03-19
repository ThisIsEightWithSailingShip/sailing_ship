package com.eight.sailingship.service.order;

import com.eight.sailingship.dto.Order.OrderRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
import com.eight.sailingship.entity.Order;

import java.util.List;

public interface OrderService {
    void save(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrderList();

    // 매장 id 기반 해당 주문들 전부 조회
    List<Order> getOrderCheckList(Long storeId);
}

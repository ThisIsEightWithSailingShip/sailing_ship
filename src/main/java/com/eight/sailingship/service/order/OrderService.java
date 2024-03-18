package com.eight.sailingship.service.order;

import com.eight.sailingship.dto.Order.OrderAfterPayRequestDto;
import com.eight.sailingship.dto.Order.OrderBeforePayRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;

import java.util.List;

public interface OrderService {
    void save(OrderBeforePayRequestDto orderBeforePayRequestDto);

    List<OrderResponseDto> getOrderList();

    void makeCart(OrderBeforePayRequestDto orderBeforePayRequestDto);

    OrderResponseDto getNotPayOrder();

    void saveOrder(OrderAfterPayRequestDto orderAfterPayRequestDto);
}

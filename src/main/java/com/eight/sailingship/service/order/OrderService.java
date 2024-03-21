package com.eight.sailingship.service.order;

import com.eight.sailingship.dto.order.OrderAfterPayRequestDto;
import com.eight.sailingship.dto.order.OrderBeforePayRequestDto;
import com.eight.sailingship.dto.order.OrderDeleteRequestDto;
import com.eight.sailingship.dto.order.OrderResponseDto;
import com.eight.sailingship.entity.User;
import com.eight.sailingship.entity.Order;

import java.util.List;

public interface OrderService {
    void save(OrderBeforePayRequestDto orderBeforePayRequestDto);

    List<OrderResponseDto> getOrderList();

    void makeCart(OrderBeforePayRequestDto orderBeforePayRequestDto, User userDetails);

    OrderResponseDto getNotPayOrder();

    void saveOrder(OrderAfterPayRequestDto orderAfterPayRequestDto);
    // 매장 id 기반 해당 주문들 전부 조회
    List<Order> getOrderCheckList(Long storeId);

    void completeOrderCheck(Long orderId);

    void deleteOrder(OrderDeleteRequestDto orderDeleteRequestDto);
}

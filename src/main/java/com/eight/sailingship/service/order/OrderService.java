package com.eight.sailingship.service.order;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.order.*;
import com.eight.sailingship.entity.User;
import com.eight.sailingship.entity.Order;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getOrderList(User userDetails);

    void createOrder(OrderBeforePayRequestDto orderBeforePayRequestDto, User userDetails);

    OrderResponseDto getNotPayOrder(User user);

    void processOrderPayment(OrderAfterPayRequestDto orderAfterPayRequestDto, UserDetailsImpl user);
    // 매장 id 기반 해당 주문들 전부 조회
    List<Order> getOrderCheckList(UserDetailsImpl userDetails);

    void completeOrderCheck(Long orderId);

    void cancelOrder(OrderDeleteRequestDto orderDeleteRequestDto, UserDetailsImpl userDetails);

    void completeOrderCheck(OrderCheckRequestDto orderCheckRequestDto, UserDetailsImpl userDetails);
}

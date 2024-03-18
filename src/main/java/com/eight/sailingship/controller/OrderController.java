package com.eight.sailingship.controller;

import com.eight.sailingship.dto.Order.OrderRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
import com.eight.sailingship.entity.Order;
import com.eight.sailingship.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/sail/order")
    public String createOrder(@RequestBody OrderRequestDto orderRequestDto, Model model) {
        orderService.save(orderRequestDto);
        return "redirect:/sail/order";
    }

    @GetMapping("/sail/order")
    public String getOrderList(Model model) {
        List<OrderResponseDto> orderList = orderService.getOrderList();
        model.addAttribute("orders",orderList);

        return "order/orders";
    }

    // 사장 입장 주문 확인 페이지 불러오기
    @GetMapping("/sail/store/order-check")
    public String getOrderCheckList(Model model) { // 인증 객체 메소드 파라미터로 추가 필요
        List<Order> orderCheckList = orderService.getOrderCheckList(1L);
        model.addAttribute("orderCheckList", orderCheckList);

        return "order/order-check";
    }
}

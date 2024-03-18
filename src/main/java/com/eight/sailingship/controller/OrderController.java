package com.eight.sailingship.controller;

import com.eight.sailingship.dto.Order.OrderRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
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
}

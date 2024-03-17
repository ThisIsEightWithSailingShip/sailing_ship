package com.eight.sailingship.controller;

import com.eight.sailingship.dto.Order.OrderDto;
import com.eight.sailingship.dto.Order.OrderMenuDto;
import com.eight.sailingship.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/sail/order")
    public String getOrder(@RequestBody OrderDto orderDto, Model model) {
        List<OrderMenuDto> menus = orderDto.getMenus();
        for (OrderMenuDto menu : menus) {
            System.out.println("메뉴 : " + menu.getMenuId() + ", 수량 : " + menu.getQuantity());
        }

        System.out.println(orderDto.getTotalPrice());
        return "menu/menu.html";
    }
}

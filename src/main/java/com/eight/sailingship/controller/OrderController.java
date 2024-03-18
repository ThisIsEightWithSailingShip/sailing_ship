package com.eight.sailingship.controller;

import com.eight.sailingship.dto.Order.OrderAfterPayRequestDto;
import com.eight.sailingship.dto.Order.OrderBeforePayRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
import com.eight.sailingship.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/sail/cart")
    public String makeCart(@RequestBody OrderBeforePayRequestDto orderBeforePayRequestDto, RedirectAttributes redirectAttributes) {
        orderService.makeCart(orderBeforePayRequestDto);
        redirectAttributes.addFlashAttribute("message", "주문이 성공적으로 처리되었습니다.");
        return "redirect:/sail/cart";
    }

    @GetMapping("/sail/cart")
    public String getNotPayOrder(Model model) {
        OrderResponseDto orderResponseDto = orderService.getNotPayOrder();
        model.addAttribute("cart",orderResponseDto);
        return "order/pay-form";
    }

    @PatchMapping("/sail/order")
    public String saveOrder(@RequestBody OrderAfterPayRequestDto orderAfterPayRequestDto){
        System.out.println(orderAfterPayRequestDto.getMessageToDriver());
        System.out.println(orderAfterPayRequestDto.getOrderId());
        orderService.saveOrder(orderAfterPayRequestDto);

        return "success";
    }

    @GetMapping("/sail/order")
    public String getOrderList(Model model) {
        List<OrderResponseDto> orderList = orderService.getOrderList();
        model.addAttribute("orders",orderList);

        return "order/orders";
    }
}

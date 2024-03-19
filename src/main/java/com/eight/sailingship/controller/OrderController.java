package com.eight.sailingship.controller;

import com.eight.sailingship.dto.Order.OrderAfterPayRequestDto;
import com.eight.sailingship.dto.Order.OrderBeforePayRequestDto;
import com.eight.sailingship.dto.Order.OrderCheckRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
import com.eight.sailingship.entity.Order;
import com.eight.sailingship.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    // 사장 입장 주문 확인 페이지 불러오기
    @GetMapping("/sail/store/order-check")
    public String getOrderCheckList(Model model) { // 인증 객체 메소드 파라미터로 추가 필요
        List<Order> orderCheckList = orderService.getOrderCheckList(1L);
        model.addAttribute("orderCheckList", orderCheckList);

        return "order/order-check";
    }

    // 사장 입장 주문 완료 버튼
    @PutMapping("/sail/store/order-complete")
    public ResponseEntity<String> updateOrderStatus(@RequestBody OrderCheckRequestDto orderCheckRequestDto) {
        System.out.println(orderCheckRequestDto.getOrderId());
        return ResponseEntity.ok("Order status updated successfully"); // Put 매핑은 리다이렉팅 허용 x
    }
}

package com.eight.sailingship.controller;

import com.eight.sailingship.dto.Order.*;
import com.eight.sailingship.entity.Order;
import com.eight.sailingship.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //장바구니에 물품 담기
    @PostMapping("/sail/cart")
    public ResponseEntity<?> makeCart(@RequestBody OrderBeforePayRequestDto orderBeforePayRequestDto) {
        try {
            orderService.makeCart(orderBeforePayRequestDto);
            return ResponseEntity.ok("주문이 성공적으로 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 매장 또는 메뉴가 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 주문을 처리할 수 없습니다.");
        }
    }

    @GetMapping("/sail/cart")
    public String getNotPayOrder(Model model) {
        OrderResponseDto orderResponseDto = orderService.getNotPayOrder();
        model.addAttribute("cart",orderResponseDto);
        return "order/pay-form";
    }

    @PatchMapping("/sail/order")
    public ResponseEntity<Void> saveOrder(@RequestBody OrderAfterPayRequestDto orderAfterPayRequestDto){
        orderService.saveOrder(orderAfterPayRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sail/order")
    public String getOrderList(Model model) {
        List<OrderResponseDto> orderList = orderService.getOrderList();
        model.addAttribute("orders",orderList);

        return "order/orders";
    }

    @DeleteMapping("/sail/order")
    public ResponseEntity<Void> deleteOrder(@RequestBody OrderDeleteRequestDto orderDeleteRequestDto) {
        orderService.deleteOrder(orderDeleteRequestDto);
        return ResponseEntity.ok().build();
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

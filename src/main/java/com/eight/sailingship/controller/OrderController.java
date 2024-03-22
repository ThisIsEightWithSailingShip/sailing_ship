package com.eight.sailingship.controller;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.order.*;
import com.eight.sailingship.entity.Order;
import com.eight.sailingship.error.order.BalanceInsufficientException;
import com.eight.sailingship.repository.OrderRepository;
import com.eight.sailingship.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    //장바구니에 물품 담기
    @PostMapping("/sail/cart")  // 예외처리 완료
    public ResponseEntity<?> createOrder(@RequestBody OrderBeforePayRequestDto orderBeforePayRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            orderService.createOrder(orderBeforePayRequestDto, userDetails.getUser());
            return ResponseEntity.ok("주문이 성공적으로 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 매장 또는 메뉴가 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 주문을 처리할 수 없습니다.");
        }
    }

    // 결제하지 않은 주문 확인
    @GetMapping("/sail/cart")   // 예외 처리 완료
    public String getNotPayOrder(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponseDto orderResponseDto = orderService.getNotPayOrder(userDetails.getUser());
        model.addAttribute("cart", orderResponseDto);
        return "order/pay-form";
    }

    // 결제
    @PatchMapping("/sail/order")
    public ResponseEntity<?> saveOrder(@RequestBody OrderAfterPayRequestDto orderAfterPayRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            orderService.processOrderPayment(orderAfterPayRequestDto, userDetails);
            return ResponseEntity.ok().build();
        }catch (BalanceInsufficientException e){
            System.out.println("잔액부족");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 주문을 처리할 수 없습니다.");
        }

    }

    // 전체 주문 내역 확인
    @GetMapping("/sail/order")
    public String getOrderList(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResponseDto> orderList = orderService.getOrderList(userDetails.getUser());
        model.addAttribute("orders", orderList);

        return "order/orders";
    }

    // 주문 취소
    @DeleteMapping("/sail/order")
    public ResponseEntity<Void> deleteOrder(@RequestBody OrderDeleteRequestDto orderDeleteRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(orderDeleteRequestDto,userDetails);
        return ResponseEntity.ok().build();
    }


    // 사장 입장 주문 확인 페이지 불러오기
    @GetMapping("/sail/order/check")
    public String getOrderCheckList(Model model) { // 인증 객체 메소드 파라미터로 추가 필요
        List<Order> orderCheckList = orderService.getOrderCheckList(1L);
        model.addAttribute("orderCheckList", orderCheckList);

        return "order/order-check";
    }

    // 사장 입장 주문 완료 버튼
    @PutMapping("/sail/order/complete")
    public ResponseEntity<String> updateOrderStatus(@RequestBody OrderCheckRequestDto orderCheckRequestDto) {
        System.out.println(orderCheckRequestDto.getOrderId());
        orderService.completeOrderCheck(orderCheckRequestDto.getOrderId());

        return ResponseEntity.ok("Order status updated successfully"); // Put 매핑은 리다이렉팅 허용 x
    }
}

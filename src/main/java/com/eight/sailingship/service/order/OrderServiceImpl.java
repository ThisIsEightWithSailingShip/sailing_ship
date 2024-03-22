package com.eight.sailingship.service.order;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.order.*;
import com.eight.sailingship.entity.*;
import com.eight.sailingship.repository.UserRepository;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.OrderRepository;
import com.eight.sailingship.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void createOrder(OrderBeforePayRequestDto orderBeforePayRequestDto, User userDetails) {

        User user = getUser(userDetails);   // user 검증
        hasActiveOrder(user);   // 이미 진행중인 주문이 있는지 확인(한 계정 당 한개의 주문이 완료되거나 취소해야 다른 주문이 가능)
        Store store = getStore(orderBeforePayRequestDto);   // 매장 검증
        Order order = new Order(orderBeforePayRequestDto,store, user);  // 주문서 기본 틀 제작
        List<OrderMenuRequestDto> orderMenus = orderBeforePayRequestDto.getMenus();
        addOrderMenusInOrder(orderMenus, order);    // 주문서에 주문 메뉴 담기
        orderRepository.save(order);    // 주문서 저장

    }


    @Override   // 결제되지 않은 주문 가져오기
    public OrderResponseDto getNotPayOrder(User userDetails) {
        User user = getUser(userDetails);   //user 검증
        Order order = getNotPayOrderByUser(user);   // 현재 계정의 결제되지 않은 주문 가져오기
        List<OrderMenuResponseDto> orderMenusResponseDto = getOrderMenusResponseDto(order); //ResponseDto에 담기
        return new OrderResponseDto(orderMenusResponseDto,order);
    }

    @Override
    @Transactional  //주문 결제
    public void processOrderPayment(OrderAfterPayRequestDto orderAfterPayRequestDto, UserDetailsImpl user) {
        Order order = getOrder(orderAfterPayRequestDto);
        Long orderUserId = order.getUser().getUserId(); // 주문서의 주인 User id
        Long userId = user.getUser().getUserId();   // 현재 로그인된 계정의 User id

        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new NullPointerException("해당하는 계정이 없습니다"));

        if(orderUserId.equals(userId)) {    // 계정이 일치하면 결제 진행
            processPayment(orderAfterPayRequestDto, findUser, order);
            return;
        }

        throw new IllegalArgumentException("회원과 일치하지 않는 주문입니다");

    }


    @Override
    public List<OrderResponseDto> getOrderList(User userDetails) {
        User user = getUser(userDetails);   // 계정 검증
        List<Order> customerOrders = orderRepository.findByUser(user); //해당 계정에 해당하는 주문 목록 가져오기
        List<OrderResponseDto> orderResponseDtoList = getOrderResponseDtos(customerOrders); // ResponseDto로 변환

        return orderResponseDtoList;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderCheckList(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        return orderRepository.findByStoreAndStatus(store, StatusEnum.PAY_COMPLETE);
    }

    // 배달 완료 처리
    @Override
    @Transactional
    public void completeOrderCheck(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.deliveryComplete();

        System.out.println(order.getStatus());
    }

    @Override
    public void cancelOrder(OrderDeleteRequestDto orderDeleteRequestDto, UserDetailsImpl userDetails) {
        Order order = getOrder(orderDeleteRequestDto);  // 취소 할 주문 가져오기

        User user = userDetails.getUser();
        Long orderUserId = order.getUser().getUserId(); // 삭제할 주문의 주인 User Id
        Long userId = user.getUserId(); // 현재 접속중인 User Id

        if(orderUserId.equals(userId)){ // User가 일치하면 주문 취소 진행
            orderRepository.delete(order);
            return;
        }

        throw new IllegalArgumentException("회원과 일치하지 않는 주문입니다");

    }

    //  -------------------------------private method-------------------------------

    private User getUser(User userDetails) {
        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(()->
                new NullPointerException("일치하지 않는 회원입니다"));
        return user;
    }

    private void hasActiveOrder(User user) {
        Optional<Order> justInCart = orderRepository.findByUserAndStatus(user,StatusEnum.JUST_IN_CART);
        if(justInCart.isPresent()){
            throw new IllegalArgumentException("이미 주문중인 장바구니가 있습니다");
        }
    }

    private Store getStore(OrderBeforePayRequestDto orderBeforePayRequestDto) {
        Store store = storeRepository.findById(orderBeforePayRequestDto.getStoreId()).orElseThrow(() ->
                new NullPointerException("해당하는 매장이 없습니다"));
        return store;
    }

    private void addOrderMenusInOrder(List<OrderMenuRequestDto> orderMenus, Order order) {
        for (OrderMenuRequestDto orderMenu : orderMenus) {
            Menu menu = menuRepository.findById(orderMenu.getMenuId()).orElseThrow(() ->
                    new NullPointerException("해당 하는 메뉴가 존재하지 않습니다"));
            order.addOrderMenuList(new OrderMenu(menu,orderMenu.getQuantity()));
        }
    }

    private Order getOrder(OrderAfterPayRequestDto orderAfterPayRequestDto) {
        Order order = orderRepository.findById(orderAfterPayRequestDto.getOrderId()).orElseThrow(
                ()->new NullPointerException("해당하는 주문이 없습니다")
        );
        return order;
    }

    private Order getOrder(OrderDeleteRequestDto orderDeleteRequestDto) {
        Order order = orderRepository.findById(orderDeleteRequestDto.getOrderId()).orElseThrow(() ->
                new NullPointerException("해당하는 주문이 없습니다"));
        return order;
    }

    private Order getNotPayOrderByUser(User user) {
        Order order = orderRepository.findByUserAndStatus(user,StatusEnum.JUST_IN_CART).orElseThrow(()->
                new NullPointerException("결제 가능한 주문이 존재하지 않습니다"));
        return order;
    }

    private void processPayment(OrderAfterPayRequestDto orderAfterPayRequestDto, User findUser, Order order) {
        findUser.execPay(order.getTotalPrice());
        order.pay_complete(orderAfterPayRequestDto);
    }

    private static List<OrderResponseDto> getOrderResponseDtos(List<Order> customerOrders) {
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for (Order customerOrder : customerOrders) {
            List<OrderMenuResponseDto> orderMenus = getOrderMenusResponseDto(customerOrder);

            orderResponseDtoList.add(new OrderResponseDto(orderMenus,customerOrder));
        }
        return orderResponseDtoList;
    }

    private static List<OrderMenuResponseDto> getOrderMenusResponseDto(Order customerOrder) {
        List<OrderMenu> orderMenuList = customerOrder.getOrderMenuList();
        List<OrderMenuResponseDto> orderMenus = new ArrayList<>();
        orderMenuList.stream().forEach(orderMenu->
        {
            orderMenus.add(new OrderMenuResponseDto(orderMenu.getMenu(),orderMenu.getQuantity()));
        });
        return orderMenus;

    }

}

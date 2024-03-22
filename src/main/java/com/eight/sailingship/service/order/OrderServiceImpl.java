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
    public void makeCart(OrderBeforePayRequestDto orderBeforePayRequestDto, User userDetails) {

        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
        Optional<Order> justInCart = orderRepository.findByUserAndStatus(user,StatusEnum.JUST_IN_CART);
        if(justInCart.isPresent()){
            throw new IllegalArgumentException("이미 주문중인 장바구니가 있습니다");
        }
        Store store = storeRepository.findById(orderBeforePayRequestDto.getStoreId()).orElseThrow(() ->
                new NullPointerException("해당하는 매장이 없습니다"));
        Order order = new Order(orderBeforePayRequestDto,store, user);
        List<OrderMenuRequestDto> orderMenus = orderBeforePayRequestDto.getMenus();
        for (OrderMenuRequestDto orderMenu : orderMenus) {
            Menu menu = menuRepository.findById(orderMenu.getMenuId()).orElseThrow(() ->
                    new NullPointerException("해당 하는 메뉴가 존재하지 않습니다"));
            order.addOrderMenuList(new OrderMenu(menu,orderMenu.getQuantity()));
        }
        orderRepository.save(order);

    }

    @Override
    public OrderResponseDto getNotPayOrder(User userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
        Order order = orderRepository.findByUserAndStatus(user,StatusEnum.JUST_IN_CART).orElseThrow(()->
                new NullPointerException("결제 가능한 주문이 존재하지 않습니다"));
        List<OrderMenuResponseDto> orderMenusResponseDto = getOrderMenusResponseDto(order);
        return new OrderResponseDto(orderMenusResponseDto,order);
    }

    @Override
    @Transactional
    public void saveOrder(OrderAfterPayRequestDto orderAfterPayRequestDto, UserDetailsImpl user) {
        Order order = orderRepository.findById(orderAfterPayRequestDto.getOrderId()).orElseThrow(
                ()->new NullPointerException("해당하는 주문이 없습니다")
        );
        if(order.getUser().getUserId().equals(user.getUser().getUserId())) {
            order.pay_complete(orderAfterPayRequestDto);
            return;
        }

        throw new IllegalArgumentException("회원과 일치하지 않는 주문입니다");

    }

    @Override
    public List<OrderResponseDto> getOrderList(User userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
        List<Order> customerOrders = orderRepository.findByUser(user); //orderRepository.findByCustomer();
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
    public void deleteOrder(OrderDeleteRequestDto orderDeleteRequestDto, UserDetailsImpl userDetails) {
        Order order = orderRepository.findById(orderDeleteRequestDto.getOrderId()).orElseThrow(() ->
                new NullPointerException("해당하는 주문이 없습니다"));

        User user = userDetails.getUser();

        if(order.getUser().getUserId().equals(user.getUserId())){
            orderRepository.delete(order);
            return;
        }

        throw new IllegalArgumentException("회원과 일치하지 않는 주문입니다");

    }
}

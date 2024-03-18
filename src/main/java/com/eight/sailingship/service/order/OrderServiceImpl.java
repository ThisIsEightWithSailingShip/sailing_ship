package com.eight.sailingship.service.order;

import com.eight.sailingship.dto.Order.OrderMenuResponseDto;
import com.eight.sailingship.dto.Order.OrderRequestDto;
import com.eight.sailingship.dto.Order.OrderMenuRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
import com.eight.sailingship.entity.*;
import com.eight.sailingship.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void save(OrderRequestDto orderRequestDto) {
        Store store = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(() ->
                new NullPointerException("해당하는 매장이 없습니다"));

        Customer customer = customerRepository.findById(1L).orElseThrow(); // 차후 인증객체 기반 고객정보 저장 필요

        Order order = new Order(orderRequestDto, store, customer);
        List<OrderMenuRequestDto> orderMenus = orderRequestDto.getMenus();
        for (OrderMenuRequestDto orderMenu : orderMenus) {
            Menu menu = menuRepository.findById(orderMenu.getMenuId()).orElseThrow(() ->
                    new NullPointerException("해당 하는 메뉴가 존재하지 않습니다"));
           order.addOrderMenuList(new OrderMenu(menu,orderMenu.getQuantity()));
        }

        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDto> getOrderList() {
        List<Order> customerOrders = orderRepository.findAll(); //orderRepository.findByCustomer();
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for (Order customerOrder : customerOrders) {
            List<OrderMenu> orderMenuList = customerOrder.getOrderMenuList();
            List<OrderMenuResponseDto> orderMenus = new ArrayList<>();
            orderMenuList.stream().forEach(orderMenu->
            {
                orderMenus.add(new OrderMenuResponseDto(orderMenu.getMenu(),orderMenu.getQuantity()));
            });

            orderResponseDtoList.add(new OrderResponseDto(orderMenus,customerOrder));
        }

        return orderResponseDtoList;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderCheckList(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        return orderRepository.findByStore(store);
    }
}

package com.eight.sailingship.service.order;

import com.eight.sailingship.dto.Order.OrderMenuResponseDto;
import com.eight.sailingship.dto.Order.OrderRequestDto;
import com.eight.sailingship.dto.Order.OrderMenuRequestDto;
import com.eight.sailingship.dto.Order.OrderResponseDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Order;
import com.eight.sailingship.entity.OrderMenu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.OrderMenusRepository;
import com.eight.sailingship.repository.OrderRepository;
import com.eight.sailingship.repository.StoreRepository;
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
    @Override
    @Transactional
    public void save(OrderRequestDto orderRequestDto) {
        Store store = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(() ->
                new NullPointerException("해당하는 매장이 없습니다"));
        Order order = new Order(orderRequestDto,store);
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
        List<Order> orderCheckList;

        return null;
    }
}

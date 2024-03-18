package com.eight.sailingship.entity;

import com.eight.sailingship.dto.Order.OrderRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor
public class Order extends OrderTimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column
    private Long totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderMenu> orderMenuList;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Order(OrderRequestDto orderRequestDto, Store store) {
        this.status = 0;
        this.totalPrice = orderRequestDto.getTotalPrice();
        this.orderMenuList = new ArrayList<>();
        this.store = store;
    }

    public void addOrderMenuList(OrderMenu orderMenu) {
        this.orderMenuList.add(orderMenu);
        orderMenu.setOrder(this);
    }


}

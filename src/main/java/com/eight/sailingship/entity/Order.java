package com.eight.sailingship.entity;

import com.eight.sailingship.dto.order.OrderAfterPayRequestDto;
import com.eight.sailingship.dto.order.OrderBeforePayRequestDto;
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
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long totalPrice;

    @Column
    private String messageToStore;

    @Column
    private String messageToDriver;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderMenu> orderMenuList;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Order(OrderBeforePayRequestDto orderBeforePayRequestDto, Store store, User user) {
        this.status = StatusEnum.JUST_IN_CART;
        this.totalPrice = orderBeforePayRequestDto.getTotalPrice();
        this.orderMenuList = new ArrayList<>();
        this.store = store;
        this.user = user;
    }

    public void addOrderMenuList(OrderMenu orderMenu) {
        this.orderMenuList.add(orderMenu);
        orderMenu.setOrder(this);
    }

    public void pay_complete(OrderAfterPayRequestDto orderAfterPayRequestDto){
        this.messageToStore = orderAfterPayRequestDto.getMessageToStore();
        this.messageToDriver = orderAfterPayRequestDto.getMessageToDriver();
        this.status = StatusEnum.PAY_COMPLETE;
    }

    public void deliveryComplete() {
        this.status = StatusEnum.DELIVERY_COMPLETE;
    }
}

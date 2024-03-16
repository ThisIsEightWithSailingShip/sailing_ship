package com.eight.sailingship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "order_date")
    @CreatedDate
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column
    private Long totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderMenu> orderMenuList;

    public void addOrderMenuList(OrderMenu orderMenu) {
        this.orderMenuList.add(orderMenu);
        orderMenu.setOrder(this);
    }
}

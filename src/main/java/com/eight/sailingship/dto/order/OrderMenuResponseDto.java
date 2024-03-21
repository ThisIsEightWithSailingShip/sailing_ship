package com.eight.sailingship.dto.order;


import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.OrderMenu;
import lombok.Getter;

@Getter
public class OrderMenuResponseDto {
    private String MenuName;
    private int quantity;
    private Long price;


    public OrderMenuResponseDto(OrderMenu orderMenu) {

    }

    public OrderMenuResponseDto(Menu menu, int quantity) {
        this.MenuName =menu.getMenuName();
        this.price = menu.getPrice();
        this.quantity = quantity;
    }
}

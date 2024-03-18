package com.eight.sailingship.dto.Order;


import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.OrderMenu;
import lombok.Getter;
import lombok.Setter;

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

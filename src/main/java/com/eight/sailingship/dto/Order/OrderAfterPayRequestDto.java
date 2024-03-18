package com.eight.sailingship.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAfterPayRequestDto {
    private Long orderId;
    private String messageToStore;
    private String messageToDriver;
}

package com.eight.sailingship.dto.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequestDto {
    private String menuName;
    private String introduce;
    private Long price;
    private String menuCategory;
}

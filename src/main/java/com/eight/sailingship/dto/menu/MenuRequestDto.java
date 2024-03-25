package com.eight.sailingship.dto.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MenuRequestDto {
    private final String menuName;
    private final String introduce;
    private final Long price;
    private final String menuCategory;
}

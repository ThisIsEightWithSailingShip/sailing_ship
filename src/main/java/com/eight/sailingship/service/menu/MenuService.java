package com.eight.sailingship.service.menu;

import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MenuService {

    void createMenu(MenuRequestDto requestDto);

    List<Menu> listMenu();
    Menu editMenu(Long menuId, Long storeId);
    void editSaveMenu(MenuRequestDto requestDto, Long id);
    ResponseEntity<String> deleteMenu(Long menuId, Long storeId);
}

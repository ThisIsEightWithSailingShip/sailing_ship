package com.eight.sailingship.service.menu;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuService {

    Long createMenu(MenuRequestDto requestDto, UserDetailsImpl userDetails);

    List<Menu> listMenu(UserDetailsImpl userDetails);
    Menu editMenu(Long menuId, UserDetailsImpl userDetails);
    void editSaveMenu(MenuRequestDto requestDto, Long id);
    ResponseEntity<String> deleteMenu(Long menuId, UserDetailsImpl userDetails);
}

package com.eight.sailingship.service.menu;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MenuService {

    long createMenu(MenuRequestDto requestDto, UserDetailsImpl userDetails, MultipartFile images) throws IOException;

    List<Menu> listMenu(UserDetailsImpl userDetails, Long storeId);
    Menu editMenu(Long menuId, UserDetailsImpl userDetails);
    long editSaveMenu(MenuRequestDto requestDto, Long id, MultipartFile images, UserDetailsImpl userDetails) throws IOException;
    ResponseEntity<String> deleteMenu(Long menuId, UserDetailsImpl userDetails);
    void checkListMenu(UserDetailsImpl userDetails, Long storeId);
}

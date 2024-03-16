package com.eight.sailingship.service.menu;

import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createMenu(MenuRequestDto requestDto, Model model) {
        Store store = storeRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호 입니다."));
        //***** 나중에 userdetailsimpl을 통해서 값 받아와서 넣어줘야함. @AuthenticationPrincipal UserDetailsImpl userDetails
        Menu menu = new Menu();
        menu.setMenuName(requestDto.getMenuName());
        menu.setIntroduce(requestDto.getIntroduce());
        menu.setPrice(requestDto.getPrice());
        menu.setMenuCategory(requestDto.getMenuCategory());
        menu.setStore(store);

        menuRepository.save(menu);
    }
}

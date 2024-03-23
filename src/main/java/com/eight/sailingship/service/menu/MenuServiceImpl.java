package com.eight.sailingship.service.menu;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Long createMenu(MenuRequestDto requestDto, UserDetailsImpl userDetails) {
        Store store = storeRepository.findById(userDetails.getUser().getStore().getStoreId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호 입니다."));
        Menu menu = new Menu(requestDto, store);
        return menuRepository.save(menu).getMenuId();
    }

    @Transactional(readOnly = true)
    public List<Menu> listMenu(UserDetailsImpl userDetails) {
        return menuRepository.findByStore_StoreId(userDetails.getUser().getStore().getStoreId());
    }

    @Transactional
    public Menu editMenu(Long menuId, UserDetailsImpl userDetails) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 번호 입니다."));
        long menuOwnerId = menu.getStore().getStoreId();
        if(userDetails.getUser().getStore().getStoreId() != menuOwnerId) {
            throw new IllegalArgumentException("매장이 사장님의 소유가 아닙니다.");
        }
        return menu;
    }

    @Transactional
    public void editSaveMenu(MenuRequestDto requestDto, Long id) {
        Menu menu = menuRepository.findById(id).get();
        menu.update(requestDto);
        menuRepository.save(menu);
    }

    @Transactional
    public ResponseEntity<String> deleteMenu(Long menuId, UserDetailsImpl userDetails) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 번호 입니다."));
        Long menuOwnerId = menu.getStore().getStoreId();
        if(userDetails.getUser().getStore().getStoreId() != menuOwnerId) {
            return ResponseEntity.badRequest().body("매장이 사장님의 소유가 아닙니다.");
        }
        menuRepository.delete(menu);
        return ResponseEntity.ok().body("삭제 되었습니다.");
    }

}

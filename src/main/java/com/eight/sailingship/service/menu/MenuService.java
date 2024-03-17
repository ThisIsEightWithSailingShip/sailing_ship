//package com.eight.sailingship.service.menu;
//
//import com.eight.sailingship.dto.menu.MenuRequestDto;
//import com.eight.sailingship.entity.Menu;
//import com.eight.sailingship.entity.Store;
//import com.eight.sailingship.repository.MenuRepository;
//import com.eight.sailingship.repository.StoreRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.Model;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MenuService {
//
//    private final MenuRepository menuRepository;
//    private final StoreRepository storeRepository;
//
//    @Transactional
//    public void createMenu(MenuRequestDto requestDto, Model model) {
//        Store store = storeRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호 입니다."));
//        //***** 나중에 userdetailsimpl을 통해서 값 받아와서 넣어줘야함. @AuthenticationPrincipal UserDetailsImpl userDetails
//        Menu menu = new Menu();
//        menu.setMenuName(requestDto.getMenuName());
//        menu.setIntroduce(requestDto.getIntroduce());
//        menu.setPrice(requestDto.getPrice());
//        menu.setMenuCategory(requestDto.getMenuCategory());
//        menu.setStore(store);
//
//        menuRepository.save(menu);
//    }
//
//    public List<Menu> listMenu() {
//        return menuRepository.findByStore_StoreId(1L);
//        // ***** 나중에 @AuthenticationPrincipal 이후 변경해 줘야함.
//
//    }
//
//    public Menu editMenu(Long menuId, Long storeId) {
//        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 번호 입니다."));
//        Long menuOwnerId = menu.getStore().getStoreId();
//        if(storeId != menuOwnerId) {
//            throw new IllegalArgumentException("매장이 사장님의 소유가 아닙니다.");
//        }
//        return menu;
//    }
//
//    @Transactional
//    public void editSaveMenu(MenuRequestDto requestDto, Long id) {
//        Menu menu = menuRepository.findById(id).get();
//        menu.update(requestDto);
//        menuRepository.save(menu);
//    }
//
//    public void deleteMenu(Long menuId, Long storeId) {
//        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 번호 입니다."));
//        Long menuOwnerId = menu.getStore().getStoreId();
//        if(storeId != menuOwnerId) {
//            throw new IllegalArgumentException("매장이 사장님의 소유가 아닙니다.");
//        }
//        menuRepository.delete(menu);
//    }
//
//}

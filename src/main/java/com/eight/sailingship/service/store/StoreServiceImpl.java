package com.eight.sailingship.service.store;

import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    // 매장 전체 페이지 조회
    @Override
    public String getStores(Model model) {
        List<Store> stores = storeRepository.findAll();
        model.addAttribute("stores", stores);

        return "stores.html";
    }

    @Override
    public String getStore(Model model, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        model.addAttribute("store",store);

        List<Menu> menuList = store.getMenuList();
        List<String> allCategory = menuList.stream().map(Menu::getMenuCategory).distinct().toList();

        model.addAttribute("menus", menuList);
        model.addAttribute("categories",allCategory);

        return "store-detail.html";
    }

    @Override
    @Transactional
    public void updateStore(Model model, StoreRequestDto requestDto) {
        Store findStore = storeRepository.findById(1L).orElseThrow(); // 인증객체 UserDetails에서 매장 아이디 추출 필요
        String roleString = requestDto.getCategory();

        StoreEnum storeEnum;
        if (roleString.equalsIgnoreCase("korea")) {
            storeEnum = StoreEnum.KOREA;
        } else if (roleString.equalsIgnoreCase("japan")) {
            storeEnum = StoreEnum.JAPAN;
        } else if (roleString.equalsIgnoreCase("china")) {
            storeEnum = StoreEnum.CHINA;
        } else {
            storeEnum = StoreEnum.ETC;
        }

        findStore.setAddress(requestDto.getAddress());
        findStore.setPhone(requestDto.getPhone());
        findStore.setCategory(storeEnum);
        findStore.setStoreName(requestDto.getStoreName());
    }
}

package com.eight.sailingship.service.store;

import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
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
}

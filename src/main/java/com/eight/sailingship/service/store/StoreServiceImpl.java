package com.eight.sailingship.service.store;

import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.entity.Customer;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

    // 매장 전체 페이지 조회
    @Override
    public String getStores(Model model) {
        List<Store> stores = storeRepository.findAll();
        model.addAttribute("stores", stores);

        return "store/stores.html";
    }

    @Override
    public String getStore(Model model, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        model.addAttribute("store",store);

        List<Menu> menuList = store.getMenuList();
        List<String> allCategory = menuList.stream().map(Menu::getMenuCategory).distinct().toList();

        model.addAttribute("menus", menuList);
        model.addAttribute("categories",allCategory);

        return "store/store-detail.html";
    }

    @Transactional
    public Store getUpdateStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow();
    }


    @Override
    @Transactional
    public void updateStore(Long storeId, StoreRequestDto requestDto) {
        // storeId 일치 여부
        System.out.println(requestDto.getAddress());
        System.out.println(requestDto.getCategory());
        System.out.println(requestDto.getStoreName());
        System.out.println(requestDto.getPhone());

        Store findStore = storeRepository.findById(storeId).orElseThrow(); // 인증객체 UserDetails에서 매장 아이디 추출 필요
        System.out.println(findStore.getStoreId());

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

        if (requestDto.getAddress() == null) {
            throw new NullPointerException();
        }
        if (requestDto.getStoreName() == null) {
            throw new NullPointerException();
        }

        findStore.setAddress(requestDto.getAddress());
        findStore.setPhone(requestDto.getPhone());
        findStore.setCategory(storeEnum);
        findStore.setStoreName(requestDto.getStoreName());
    }

    @Override
    @Transactional
    public Store createStore(StoreRequestDto requestDto, String ownerEmail) {
    // category 값이 null인 경우 기본값 설정
        String categoryStr = requestDto.getCategory() == null ? "ETC" : requestDto.getCategory().toUpperCase();
        StoreEnum category = StoreEnum.valueOf(categoryStr);

        Store store = new Store();
        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());
        store.setCategory(category);
        store.setStoreName(requestDto.getStoreName());
        store.setOwnerName(requestDto.getOwnerName());

        Store savedStore = storeRepository.save(store);

        Customer owner = customerRepository.findByEmail(ownerEmail);

        if (owner == null) {
            throw new RuntimeException("Owner not found with email: " + ownerEmail);
        }
        owner.setStore(savedStore);
        customerRepository.save(owner);

        return savedStore;
    }
}

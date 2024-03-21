package com.eight.sailingship.service.store;


import com.eight.sailingship.dto.store.StoreRequestDto;

import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.entity.User;
import com.eight.sailingship.repository.StoreRepository;
import com.eight.sailingship.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

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

        String categoryStr = requestDto.getCategory() == null ? "ETC" : requestDto.getCategory().toUpperCase();
        StoreEnum category = StoreEnum.valueOf(categoryStr);

        Store store = new Store();
        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());
        store.setCategory(category);
        store.setStoreName(requestDto.getStoreName());
        store.setOwnerName(requestDto.getOwnerName());

        // 이미 매장을 소유한 유저인지 확인하기 위해 이메이을 불러옴.
        Optional<User> ownerOptional = userRepository.findByEmail(ownerEmail);
        User owner = ownerOptional.orElseThrow(() -> new RuntimeException("Owner not found with email: " + ownerEmail));

        if (owner.getStore() != null) {
            // 매장을 소유하고 있는 유저인지 확인.
            throw new IllegalStateException("User already has a store assigned. Cannot create another.");
        }

        Store savedStore = storeRepository.save(store);
        owner.setStore(savedStore);
        userRepository.save(owner);

        return savedStore;
    }

    @Override
    public boolean checkIfUserHasStore(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getStore() != null;
        }
        return false;
    }

    public Long findStoreIdByUserEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .map(User::getStore)
                .map(Store::getStoreId)
                .orElseThrow(() -> new RuntimeException("No store found for user."));
    }



}

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
    public Store getUpdateStore(Long storeId, String ownerEmail) {
        return storeRepository.findById(storeId).orElseThrow();
    }


    @Override
    @Transactional
    public void updateStore(Long storeId, StoreRequestDto requestDto, String ownerEmail) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));


        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found with email: " + ownerEmail));

        if (!store.getOwner().equals(owner)) {
            throw new IllegalStateException("User does not have permission to update this store.");
        }


        String categoryStr = requestDto.getCategory() == null ? "ETC" : requestDto.getCategory().toUpperCase();
        StoreEnum category = StoreEnum.valueOf(categoryStr);


        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());
        store.setCategory(category);
        store.setStoreName(requestDto.getStoreName());
        store.setOwnerName(store.getOwnerName());

        storeRepository.save(store);
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

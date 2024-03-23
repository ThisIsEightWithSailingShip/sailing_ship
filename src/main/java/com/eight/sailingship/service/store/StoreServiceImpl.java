package com.eight.sailingship.service.store;


import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.store.StoreRequestDto;

import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.entity.User;
import com.eight.sailingship.repository.StoreRepository;
import com.eight.sailingship.repository.UserRepository;
import com.eight.sailingship.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

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
    public Store getUpdateStore(Long storeId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));

        if (!store.getOwner().equals(owner)) {
            throw new IllegalStateException("User does not have permission to update this store.");
        }

        return store;

    }

    @Override
    @Transactional
    public void updateStore(Long storeId, StoreRequestDto requestDto, Long userId, MultipartFile image) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!store.getOwner().equals(user)) {
            throw new IllegalStateException("User does not have permission to update this store.");
        }

        if (image != null && !image.isEmpty()) {
            imageService.updateStoreImage(image, storeId);
        }

        String categoryStr = requestDto.getCategory() == null ? "ETC" : requestDto.getCategory().toUpperCase();
        StoreEnum category = StoreEnum.valueOf(categoryStr);
        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());
        store.setCategory(category);
        store.setStoreName(requestDto.getStoreName());


        storeRepository.save(store);
    }



    @Override
    @Transactional
    public Long createStore(StoreRequestDto requestDto, UserDetailsImpl userDetails) {


        if (storeRepository.findByOwner_UserId(userDetails.getUser().getUserId()).isPresent()) {
            throw new IllegalAccessError("이미 매장을 생성하였습니다.");
        }
        // 사용자 ID를 기반으로 사용자 조회
        User owner = userRepository.findById(userDetails.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("Owner not found with ID"));

        // 매장 생성 로직
        Store store = new Store(requestDto, owner);;

        return storeRepository.save(store).getStoreId();
    }


    @Override
    public boolean checkIfUserHasStore(Long userId) {
        // storeRepository.findByOwner_UserId(userId) 호출로 Optional<Store> 객체를 얻습니다.
        Optional<Store> storeOptional = storeRepository.findByOwner_UserId(userId);

        // Optional 객체가 값을 포함하고 있는지 확인합니다.
        return storeOptional.isPresent();
    }
    public Long findStoreIdByUserId(Long userId) {

        Store store = storeRepository.findByOwner_UserId(userId).orElseThrow(() -> new RuntimeException("해당 아이디를 가진 유저 아이디가 없습니다. :" + userId));
        return store.getStoreId();
    }

    @Override
    public boolean checkStorePermission(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        if (!store.getOwner().getUserId().equals(userId)) {
            throw new IllegalStateException("User does not have permission to update this store.");
        }
        return true;
    }




}
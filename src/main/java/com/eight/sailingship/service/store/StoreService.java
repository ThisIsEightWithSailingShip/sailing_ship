package com.eight.sailingship.service.store;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.entity.Store;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
    // 전체 매장 페이지 조회
    String getStores(Model model);

    // 특정 매장 페이지 조회
    String getStore(Model model, Long storeId);

    Store getUpdateStore(Long storeId, Long userId);

    // 매장 정보 수정
    void updateStore(Long storeId, StoreRequestDto requestDto, Long userId, MultipartFile image) throws Exception;

    //매장 생성
    Long createStore(StoreRequestDto requestDto, UserDetailsImpl userDetails) throws Exception;

    boolean checkIfUserHasStore(Long userId);

    Long findStoreIdByUserId(Long userId);

    boolean checkStorePermission(Long storeId, Long userId);




}
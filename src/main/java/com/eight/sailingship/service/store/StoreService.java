package com.eight.sailingship.service.store;

import com.eight.sailingship.dto.store.StoreRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface StoreService {
    // 전체 매장 페이지 조회
    String getStores(Model model);

    // 특정 매장 페이지 조회
    String getStore(Model model, Long storeId);

    // 매장 정보 수정
    void updateStore(Long storeId, StoreRequestDto requestDto);
}

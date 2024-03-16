package com.eight.sailingship.service.store;

import org.springframework.ui.Model;

public interface StoreService {
    // 전체 매장 페이지 조회
    String getStores(Model model);

    // 특정 매장 페이지 조회
    String getStore(Model model, Long storeId);

    //
}

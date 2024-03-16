package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Store;
import com.eight.sailingship.service.StoreService;
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
        model.addAttribute("store", stores);

        return "stores.html";
    }
}

package com.eight.sailingship.controller;

import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sail/store")
public class StoreController {

    private final StoreService storeService;

    // 전체 매장 페이지 조회
    @GetMapping
    public String getStores(Model model) {
        return storeService.getStores(model);
    }

    // 특정 매장 페이지 조회
    @GetMapping("/{storeId}")
    public String getStore(@PathVariable Long storeId, Model model){
        return storeService.getStore(model, storeId);
    }

    // 매장 수정 페이지 조회
    @GetMapping("/{storeId}/update")
    public String updateStore(@PathVariable Long storeId, Model model) {
        // storeId의 값이 UserDetails 객체의 값에서 타고 타고 조회해야 함
        return "store-update.html";
    }

    @PutMapping
    public String updateStore(Model model, @ModelAttribute StoreRequestDto requestDto) {
        storeService.updateStore(model, requestDto);
        return "redirect:/sail/store";
    }
}

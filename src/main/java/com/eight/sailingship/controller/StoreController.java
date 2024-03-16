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

    @GetMapping
    public String getStores(Model model) {
        return storeService.getStores(model);
    }

    @GetMapping("/{storeId}")
    public String getStore(@PathVariable Long storeId, Model model){
        return storeService.getStore(model, storeId);
    }

    @PutMapping
    public String updateStore(Model model, @ModelAttribute StoreRequestDto requestDto) {
        storeService.updateStore(model, requestDto);
        return "redirect:/sail/store";
    }
}

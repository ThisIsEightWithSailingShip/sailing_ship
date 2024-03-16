package com.eight.sailingship.controller;

import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping("/sail/store/{storeId}")
    public String getStoreDetail(@PathVariable Long storeId, Model model){
        Store store = testRepository.findById(storeId).orElseThrow();
        model.addAttribute("store",store);
        model.addAttribute("menu",store.getMenuList());
        return "menu/store-detail.html";
    }

}
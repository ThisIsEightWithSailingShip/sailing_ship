package com.eight.sailingship.controller;

import com.eight.sailingship.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sail/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public String getStores(Model model) {
        return storeService.getStores(model);
    }
}

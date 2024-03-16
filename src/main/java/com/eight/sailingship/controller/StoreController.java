package com.eight.sailingship.controller;

import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sail/store")
public class StoreController {

    private final StoreRepository tempRepository;

    @GetMapping
    public String stores(Model model) {
        List<Store> stores = tempRepository.findAll();
        model.addAttribute("store", stores);

        return "stores.html";
    }
}

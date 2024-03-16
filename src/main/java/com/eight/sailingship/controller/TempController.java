package com.eight.sailingship.controller;

import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TempController {

    private final TempRepository tempRepository;

    @GetMapping
    public String stores(Model model) {
        List<Store> stores = tempRepository.findAll();
        model.addAttribute("stores", stores);

        return "main.html";
    }
}

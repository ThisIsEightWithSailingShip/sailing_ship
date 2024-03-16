package com.eight.sailingship.controller;

import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/sail/menu")
    public String createMenu(Model model){
        return "menu/createMenu";
    }

    @PostMapping("/sail/menu")
    public String createMenu(@ModelAttribute MenuRequestDto requestDto, Model model) {
        menuService.createMenu(requestDto, model);
        return "redirect:/sail/menu";
    }
}

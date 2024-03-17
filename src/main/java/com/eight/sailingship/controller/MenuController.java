//package com.eight.sailingship.controller;
//
//import com.eight.sailingship.dto.menu.MenuRequestDto;
//import com.eight.sailingship.entity.Menu;
//import com.eight.sailingship.service.menu.MenuService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class MenuController {
//
//    private final MenuService menuService;
//
//    @GetMapping("/sail/menu")
//    public String createMenu(Model model){
//        return "menu/createMenu";
//    }
//
//    // 나중에 authorization 필요
//    @PostMapping("/sail/menu")
//    public String createMenu(@ModelAttribute MenuRequestDto requestDto, Model model) {
//        menuService.createMenu(requestDto, model);
//        return "redirect:/sail/menu";
//    }
//
//    //나중에 authorization 필요
//    @GetMapping("/sail/listmenu")
//    public String listMenu(Model model) {
//        List<Menu> menus = menuService.listMenu();
//        model.addAttribute("menus", menus);
//        model.addAttribute("owner", true); // *** 나중에 사장이면, 배달 완료 클릭 가능. customer면 수량 체크 배달 가능.
//        return "menu/listMenu";
//    }
//
//
//    @GetMapping("/sail/menu/{id}") // 바꾸는 창을 띄워주고
//    public String editMenu(@PathVariable Long id, Model model) {
//        Long storeId = 1L; // 나중에authorization 써서, 실제 storeId를 넘겨줘야함.
//        Menu menu = menuService.editMenu(id, storeId);
//        model.addAttribute("menu", menu);
//        return "menu/editMenu";
//    }
//
//    @PostMapping("/sail/menu/{id}") // 바뀐 값들을 적용해주는
//    public String editMenu(@PathVariable Long id, @ModelAttribute MenuRequestDto requestDto) {
//        menuService.editSaveMenu(requestDto, id);
//        return "redirect:/sail/listmenu";
//    }
//
//
//    @GetMapping("/sail/editError")
//    public String showEditError(@ModelAttribute("error") String error, Model model) {
//        model.addAttribute("error", error); // RedirectAttributes에서 전달받은 오류 메시지를 모델에 추가
//        return "menu/errorMenu"; // 오류 메시지를 표시할 템플릿
//    }
//
//
//    @GetMapping("/sail/menu/trash/{id}")
//    public String deleteMenu(@PathVariable Long id) {
//        Long storeId = 1L; // 나중에 authorization 써서, 실제 storeId 넘겨줘야하마.
//        menuService.deleteMenu(id, storeId);
//        return "redirect:/sail/listmenu";
//    }
//
//
//}

package com.eight.sailingship.controller;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.service.menu.MenuServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuServiceImpl menuService;

    @Secured("ROLE_OWNER")
    @GetMapping("/sail/menu") // 메뉴 보여주는 창 -> 따라서 getmapping 이용. 조회 목적
    public String createMenu(Model model){
        return "menu/createMenu";
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/sail/menu")//새로운 메뉴를 추가해주는 api. 따라서 postmapping 이용.
    public String createMenu(@ModelAttribute MenuRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        menuService.createMenu(requestDto, userDetails);
        return "redirect:/sail/listmenu";
    }

    //나중에 authorization 필요
    @Secured("ROLE_OWNER")
    @GetMapping("/sail/listmenu") // owner만 접슨할 수 있도록 authorization해야함.
    public String listMenu(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Menu> menus = menuService.listMenu(userDetails);
        model.addAttribute("menus", menus);
        model.addAttribute("owner", true); // *** 나중에 사장이면, 배달 완료 클릭 가능. customer면 수량 체크 배달 가능.
        return "menu/listMenu";
    }


    @Secured("ROLE_OWNER")
    @GetMapping("/sail/menu/{id}") // 바꾸는 창을 띄워주고 -> 메뉴 보여주는 창 -> 따라서 getmapping 이용. 조회 목적.
    public String editMenu(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
 // 나중에authorization 써서, 실제 storeId를 넘겨줘야함.
        Menu menu = menuService.editMenu(id, userDetails);
        model.addAttribute("menu", menu);
        return "menu/editMenu";
    }

    @Secured("ROLE_OWNER")
    @PatchMapping("/sail/menu/{id}") // 바뀐 값들을 적용해주는
    public String editMenu(@PathVariable Long id, @ModelAttribute MenuRequestDto requestDto) {
        menuService.editSaveMenu(requestDto, id);
        return "redirect:/sail/listmenu";
    }


    @Secured("ROLE_OWNER")
    @GetMapping("/sail/Error")
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        model.addAttribute("error", error); // RedirectAttributes에서 전달받은 오류 메시지를 모델에 추가
        return "menu/errorMenu"; // 오류 메시지를 표시할 템플릿
    }


    @Secured("ROLE_OWNER")
    @DeleteMapping("/sail/menu/trash/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.deleteMenu(id, userDetails);
    }


}

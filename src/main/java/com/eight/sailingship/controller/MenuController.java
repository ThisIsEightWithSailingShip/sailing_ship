package com.eight.sailingship.controller;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.service.image.ImageService;
import com.eight.sailingship.service.menu.MenuServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuServiceImpl menuService;


    @Secured("ROLE_OWNER")
    @GetMapping("/sail/menu") // 메뉴 생성 html
    public String createMenu(){
        return "menu/createMenu";
    }

    @Secured("ROLE_OWNER")
    @PostMapping("/sail/menu") // 메뉴 생성
    public String createMenu(@ModelAttribute MenuRequestDto requestDto,
                             @RequestParam(value = "image") MultipartFile images,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        long storeId;
        try {
            storeId = menuService.createMenu(requestDto, userDetails, images);
        } catch (IllegalArgumentException | IOException e) {
            return "redirect:/sail/Error";
        }
        return "redirect:/sail/listmenu/" + storeId;

    }

    @Secured("ROLE_OWNER")
    @GetMapping("/sail/listmenu/{storeId}") // 메뉴 조회
    public String listMenu(Model model,
                           @PathVariable Long storeId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Menu> menus = menuService.listMenu(userDetails, storeId);
            model.addAttribute("menus", menus);
            model.addAttribute("owner", true);
            return "menu/listMenu";
        } catch (IllegalArgumentException e) {
            return "redirect:/sail/Error";
        }
    }


    @Secured("ROLE_OWNER")
    @GetMapping("/sail/list/menu/{storeId}") // 메뉴 인증
    public ResponseEntity<?> checkListMenu(@PathVariable Long storeId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            menuService.checkListMenu(userDetails, storeId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }


    @Secured("ROLE_OWNER")
    @GetMapping("/sail/menu/{id}") // 메뉴 수정 html
    public String editMenu(@PathVariable Long id, Model model,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Menu menu = menuService.editMenu(id, userDetails);
        model.addAttribute("menu", menu);
        return "menu/editMenu";
    }

    @Secured("ROLE_OWNER")
    @PatchMapping("/sail/menu/{id}") // 메뉴 수정
    public ResponseEntity<String> editMenu(@PathVariable Long id,
                           @RequestParam(value = "image") MultipartFile images,
                           @ModelAttribute MenuRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            long storeId = menuService.editSaveMenu(requestDto, id, images, userDetails);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/sail/listmenu/" + storeId)); // 리다이렉션할 URL 설정
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/sail/Error") // 다른 owner의 매장 수정 시도 시, 에러 페이지
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        String errorMessage = "접근 권한이 없습니다.";
        model.addAttribute("error", errorMessage);
        return "menu/errorMenu";
    }

    @Secured("ROLE_OWNER")
    @DeleteMapping("/sail/menu/trash/{id}") // 메뉴 삭제
    public ResponseEntity<String> deleteMenu(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.deleteMenu(id, userDetails);
    }

}

package com.eight.sailingship.controller;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.service.image.ImageService;
import com.eight.sailingship.service.menu.MenuServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuServiceImpl menuService;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);


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

        try {
            menuService.createMenu(requestDto, userDetails, images);
        } catch (IOException e) {
            logger.error("파일 업로드 중 오류 발생", e);
        }

        return "redirect:/sail/listmenu";
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/sail/listmenu") // 메뉴 조회
    public String listMenu(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ImagePhoto> images = imageService.listImage(userDetails);
        List<Menu> menus = menuService.listMenu(userDetails);
        model.addAttribute("menus", menus);
        model.addAttribute("owner", true);
        model.addAttribute("images", images);
        return "menu/listMenu";
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
    public String editMenu(@PathVariable Long id,
                           @RequestParam(value = "image") MultipartFile images,
                           @ModelAttribute MenuRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            menuService.editSaveMenu(requestDto, id);
            imageService.editImageMenu(images, id, userDetails);
        } catch (IOException e) {
            logger.error("파일 업로드 중 오류 발생", e);
        }

        return "redirect:/sail/listmenu";
    }

    @Secured("ROLE_OWNER")
    @GetMapping("/sail/Error") // 다른 owner의 매장 수정 시도 시, 에러 페이지
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        model.addAttribute("error", error);
        return "menu/errorMenu";
    }

    @Secured("ROLE_OWNER")
    @DeleteMapping("/sail/menu/trash/{id}") // 메뉴 삭제
    public ResponseEntity<String> deleteMenu(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.deleteMenu(id, userDetails);
    }

}

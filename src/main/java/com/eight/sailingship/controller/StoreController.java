package com.eight.sailingship.controller;

import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 전체 매장 페이지 조회(메인페이지)
    @GetMapping("/")
    public String getStores(Model model) {
        return storeService.getStores(model);
    }

    // 특정 매장 페이지 조회
    @GetMapping("/sail/store/{storeId}")
    public String getStore(@PathVariable Long storeId, Model model){
        return storeService.getStore(model, storeId);
    }

    // 매장 수정 페이지 조회
    @GetMapping("/sail/store/update/{storeId}")
    public String updateStore(@PathVariable Long storeId, Model model) {
        // storeId의 값이 UserDetails 객체의 값에서 타고 타고 조회해야 함
        Store store = storeService.getUpdateStore(1L);
        model.addAttribute("store", store);
        return "store/store-update";
    }

    // 수정 처리
    @PutMapping("/sail/store/update/{storeId}")
    public String updateStore(@PathVariable Long storeId, @ModelAttribute StoreRequestDto requestDto) {
        // storeId의 값이 UserDetails 객체의 값에서 타고 타고 검증해야 함
        System.out.println("컨트롤러에서도 null이겠지? : " + requestDto.getStoreName());

        storeService.updateStore(1L, requestDto);
        return "redirect:/sail/store";
    }

    // 매장 생성
    @PostMapping("/sail/store")
    @PreAuthorize("hasRole('OWNER')")
    public String createStore(@RequestBody StoreRequestDto requestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String ownerEmail = ((UserDetails) principal).getUsername();
        storeService.createStore(requestDto, ownerEmail);
        return "redirect:/";
    }
    @GetMapping("/sail/store")
    public String showCreateStoreForm(Model model) {
        var categories = Arrays.stream(StoreEnum.values())
                .collect(Collectors.toMap(Enum::name, e -> e.getRole()));

        model.addAttribute("categories", categories);

        return "store/store";
    }

}

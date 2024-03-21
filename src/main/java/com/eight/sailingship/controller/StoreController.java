package com.eight.sailingship.controller;

import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public String updateStore(@PathVariable Long storeId, Model model, Principal principal) {
        String ownerEmail = principal.getName();
        Store store = storeService.getUpdateStore(storeId, ownerEmail);
        model.addAttribute("store", store);

        var categories = Arrays.stream(StoreEnum.values())
                .collect(Collectors.toMap(Enum::name, e -> e.getRole()));
        model.addAttribute("categories", categories);

        return "store/store-update";
    }

    // 수정 처리
    @PutMapping("/sail/store/update/{storeId}")
    public String updateStore(@PathVariable Long storeId, @ModelAttribute StoreRequestDto requestDto, Principal principal) {
        String ownerEmail = principal.getName();
        //AuthenticationPrincipal 어노테이션 수정
        storeService.updateStore(storeId, requestDto, ownerEmail);
        return "redirect:/sail/store/" + storeId;
    }


    // 매장 생성
    @PostMapping("/sail/store")
    @Secured("ROLE_OWNER")
    public ResponseEntity<?> createStore(@RequestBody StoreRequestDto requestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //AuthenticationPrincipal 어노테이션 수정
        String ownerEmail = ((UserDetails) principal).getUsername();
        Store createdStore = storeService.createStore(requestDto, ownerEmail);
        // 생성된 매장의 ID를 JSON 형태로 반환
        Map<String, Long> response = new HashMap<>();
        response.put("storeId", createdStore.getStoreId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sail/store")
    @Secured("ROLE_OWNER")
    public String showCreateStoreForm(Model model) {
        //var categories = Arrays.stream(StoreEnum.values())
          //      .collect(Collectors.toMap(Enum::name, e -> e.getRole()));
        //리스트로 수정
        List<String> categoriesList = Arrays.stream(StoreEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        model.addAttribute("categoriesList", categoriesList);

        return "store-create";
    }

    @GetMapping("/owner-btn2")
    public String redirectToAppropriatePage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //AuthenticationPrincipal 어노테이션 수정
        String userEmail = ((UserDetails) principal).getUsername();

        boolean hasStore = storeService.checkIfUserHasStore(userEmail);
        if (hasStore) {
            Long storeId = storeService.findStoreIdByUserEmail(userEmail);
            return "redirect:/sail/store/" + storeId;
        } else {
            return "redirect:/sail/store";
        }
    }



}

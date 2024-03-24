package com.eight.sailingship.controller;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.store.StoreRequestDto;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.entity.RoleEnum;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.entity.StoreEnum;
import com.eight.sailingship.service.image.ImageService;
import com.eight.sailingship.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    // 전체 매장 페이지 조회(메인페이지)
    @GetMapping("/")
    public String getStores(Model model) {

        return storeService.getStores(model);
    }

    // 특정 매장 페이지 조회
    @GetMapping("/sail/store/{storeId}")
    public String getStore(@PathVariable Long storeId, Model model) {
        String viewName = storeService.getStore(model, storeId);

        try {
            // 매장의 이미지 정보 조회
            List<ImagePhoto> images = imageService.listImagesByStoreId(storeId);
            model.addAttribute("images", images);
        } catch (Exception e) {
            logger.error("Error retrieving images for store ID: {}", storeId, e);

        }

        return viewName;
    }


    // 매장 수정 페이지 조회
    @Secured(RoleEnum.Role.OWNER)
    @GetMapping("/sail/store/update/{storeId}")
    public String updateStore(@PathVariable Long storeId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            storeService.checkStorePermission(storeId, userDetails.getUser().getUserId());

            Long userId = userDetails.getUser().getUserId();
            Store store = storeService.getUpdateStore(storeId, userId);

            model.addAttribute("store", store);

            List<String> categoriesList = Arrays.stream(StoreEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());

            model.addAttribute("categoriesList", categoriesList);

            return "store/store-update";
        } catch (IllegalStateException e) {
            return "redirect:/sail/Error";
        }
    }

    // 수정 처리
    @PutMapping("/sail/store/update/{storeId}")
    public String updateStore(@PathVariable Long storeId,
                              @ModelAttribute StoreRequestDto requestDto,
                              @RequestParam(value = "image", required = false) MultipartFile image,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getUser().getUserId(); // UserDetailsImpl을 통해 사용자 ID를 가져옵니다.
            storeService.updateStore(storeId, requestDto, userId, image); // 사용자 ID를 사용하여 서비스 메서드를 호출
            return "redirect:/sail/store/" + storeId;
        } catch (Exception e) {
            return "redirect:/error"; // 에러 발생 시, 에러 페이지로 리디렉션
        }
    }

    //수정하기 버튼 클릭시 확인하는거
    @GetMapping("/sail/store/check-permission/{storeId}")
    @ResponseBody

    public ResponseEntity<?> checkPermission(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            boolean hasPermission = storeService.checkStorePermission(storeId, userDetails.getUser().getUserId());
            return ResponseEntity.ok(Map.of("hasPermission", hasPermission));
        } catch (Exception e) {
            logger.error("Error checking permission", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error checking permission"));
        }
    }


    // 매장 생성
    @GetMapping("/sail/store")
    @Secured("ROLE_OWNER")
    public String showCreateStoreForm(Model model) {
        List<String> categoriesList = Arrays.stream(StoreEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        model.addAttribute("categoriesList", categoriesList);

        return "store/store-create";
    }

    @PostMapping("/sail/store")
    @Secured("ROLE_OWNER")
    public ResponseEntity<?> createStore(@ModelAttribute StoreRequestDto requestDto,
                                         @RequestParam(value = "image", required = false) MultipartFile image,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getUser().getUserId(); // UserDetailsImpl에서 사용자 ID 추출
            Store createdStore = storeService.createStore(requestDto, userId, image);
            Map<String, Long> response = new HashMap<>();
            response.put("storeId", createdStore.getStoreId());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("파일 업로드 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("매장 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/owner-btn2")
    public String redirectToAppropriatePage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId(); // UserDetailsImpl로부터 사용자 ID를 추출합니다.

        boolean hasStore = storeService.checkIfUserHasStore(userId); // 사용자 ID를 기반으로 매장 소유 여부를 확인합니다.
        if (hasStore) {
            Long storeId = storeService.findStoreIdByUserId(userId); // 사용자 ID를 기반으로 매장 ID를 찾습니다.
            return "redirect:/sail/store/" + storeId;
        } else {
            return "redirect:/sail/store";
        }
    }



}
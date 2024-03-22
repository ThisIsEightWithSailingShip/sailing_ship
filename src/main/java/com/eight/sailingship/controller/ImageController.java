package com.eight.sailingship.controller;


import com.amazonaws.services.ec2.model.ResponseError;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.Image.ImageUploadResponse;
import com.eight.sailingship.service.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

@Tag(name = "이미지 컨트롤러", description = "이미지 등록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sail")
public class ImageController {

    private final ImageService imageService;

    //@AuthenticationPrincipal UserDetailsImpl userDetails 이거 넣어서 추후에 처리.
    @PostMapping("/image")
    public ResponseEntity<ImageUploadResponse> saveImage(@RequestParam(value = "image") MultipartFile images,
                                                         @RequestParam(value = "menu_menuId") Long menuId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return imageService.saveMenuImage(images, menuId, userDetails);
    }
}

package com.eight.sailingship.controller;

import com.eight.sailingship.dto.user.UserSignUpRequestDto;
import com.eight.sailingship.service.customer.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/sail/login")
    public String login(){
        return "login";
    }

    @PostMapping("/sail/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        System.out.println("come");
        try {
            System.out.println(userSignUpRequestDto.getEmail());
            userService.signup(userSignUpRequestDto);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sail/signup")
    public String showSignupPage() {
        return "signup";
    }
}
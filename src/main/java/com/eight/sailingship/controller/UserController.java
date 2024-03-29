package com.eight.sailingship.controller;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.user.UserInfoDto;
import com.eight.sailingship.dto.user.UserSignUpRequestDto;
import com.eight.sailingship.entity.RoleEnum;
import com.eight.sailingship.service.customer.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/sail/login")
    public String login(){
        return "user/login";
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
        return "user/signup";
    }

    // 회원 인증 정보 받기
    @GetMapping("/sail/authInfo")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return new UserInfoDto(false, false);
        }

        String emailData = userDetails.getUser().getEmail();
        RoleEnum role = userDetails.getUser().getRole();
        boolean email = true;
        boolean isOwner = (role == RoleEnum.OWNER);

        System.err.println(email + ", " + role + ", " + isOwner);

        return new UserInfoDto(email, isOwner);
    }
}
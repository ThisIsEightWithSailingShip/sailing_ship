package com.eight.sailingship.controller;

import com.eight.sailingship.dto.email.EmailCheckDto;
import com.eight.sailingship.dto.email.EmailRequestDto;
import com.eight.sailingship.service.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService mailService;

    @PostMapping("/sail/user/email-verify")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        System.out.println("이메일 인증 이메일 :" + emailDto.getEmail());
        return mailService.joinEmail(emailDto.getEmail());
    }

    @PostMapping("/sail/user/email-check")
    public String checkMail(@RequestBody @Valid EmailCheckDto checkDto) {
        boolean checked = mailService.checkAuthNum(checkDto.getEmail(), checkDto.getAuthNum());

        if(checked){
            return "ok";
        }
        else{
            throw new IllegalArgumentException("잘못된 이메일 인증.");
        }
    }
}
package com.eight.sailingship.controller;

import com.eight.sailingship.dto.common.StatusResponse;
import com.eight.sailingship.dto.email.EmailCheckDto;
import com.eight.sailingship.dto.email.EmailRequestDto;
import com.eight.sailingship.service.email.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return mailService.joinEmail(emailDto);
    }

    @PostMapping("/sail/user/email-check")
    public ResponseEntity<StatusResponse> checkMail(@RequestBody @Valid EmailCheckDto checkDto) {
        boolean checked = mailService.checkAuthNum(checkDto);

        System.err.println(checked);

        if(checked){
            StatusResponse statusResponse = new StatusResponse(HttpServletResponse.SC_OK, "올바른 인증번호 입력");
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        }
        else{
            StatusResponse statusResponse = new StatusResponse(HttpServletResponse.SC_BAD_REQUEST, "잘못된 이메일 인증 번호입니다.");
            return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
package com.eight.sailingship.controller;

import com.eight.sailingship.dto.customer.CustomerDto;
import com.eight.sailingship.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sail")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 페이지로 이동
    }

    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("customerDto") CustomerDto customerDto) {

        return "signup"; // 회원가입 페이지로 이동
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("customerDto") CustomerDto customerDto) {
        customerService.signup(customerDto); // 회원가입 처리
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 이동
    }
}

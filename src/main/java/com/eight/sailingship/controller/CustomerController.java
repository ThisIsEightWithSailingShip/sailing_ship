package com.eight.sailingship.controller;

import com.eight.sailingship.dto.customer.CustomerDto;
import com.eight.sailingship.service.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody CustomerDto customerDto) {
        try {
            System.out.println(customerDto.getEmail());
            customerService.signup(customerDto);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }
}
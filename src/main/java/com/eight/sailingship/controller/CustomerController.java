package com.eight.sailingship.controller;

import com.eight.sailingship.dto.customer.CustomerDto;
import com.eight.sailingship.service.customer.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping("/signup")
    public String signup(CustomerDto customerDto) {

        System.out.println(customerDto.getEmail());
        customerService.signup(customerDto);

        return "ok";
    }
}
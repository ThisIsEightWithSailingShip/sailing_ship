package com.eight.sailingship.service.customer;

import com.eight.sailingship.dto.customer.CustomerDto;
import com.eight.sailingship.entity.Customer;
import com.eight.sailingship.entity.RoleEnum;
import com.eight.sailingship.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(CustomerDto customerDto) {
        if (customerDto.getEmail() == null || customerDto.getPassword() == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

        // 이미 존재하는 이메일인지 확인
        boolean isUser = customerRepository.existsByEmail(customerDto.getEmail());
        if (isUser) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
        customer.setRole(RoleEnum.Role.CUSTOMER);
        customer.setNickname(customerDto.getNickname());
        customer.setAddress(customerDto.getAddress());
        customer.setPhone(customerDto.getPhone());
        customer.setAccount(1000000);

        customerRepository.save(customer);
    }




}
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
        if (customerDto.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        // DB에 이미 동일한 이메일을 가진 회원이 존재하는지 확인
        boolean isUser = customerRepository.existsByEmail(customerDto.getEmail());
        if (isUser) {
            return; // 이미 가입된 회원이므로 처리 중단
        }

        // 회원 가입 정보 생성
        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(bCryptPasswordEncoder.encode(customerDto.getPassword()));
        customer.setRole(RoleEnum.CUSTOMER);
        customer.setNickname(customerDto.getNickname());
        customer.setAddress(customerDto.getAddress());
        customer.setPhone(customerDto.getPhone());
        customer.setAccount(10000);

        // 회원 저장
        customerRepository.save(customer);
    }
}
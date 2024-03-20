package com.eight.sailingship.service.customer;


import com.eight.sailingship.dto.customer.CustomerDto;
import com.eight.sailingship.entity.Customer;
import com.eight.sailingship.entity.RoleEnum;
import com.eight.sailingship.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final com.eight.sailingship.repository.CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;



    public void signup(CustomerDto customerDto) {
// 이메일과 비밀번호가 null 또는 빈 문자열인 경우 예외 발생
        if (customerDto.getEmail() == null || customerDto.getEmail().isEmpty() ||
                customerDto.getPassword() == null || customerDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email and password cannot be null or empty");
        }

        // 이메일이 이미 존재하는지 확인
        boolean isUser = customerRepository.existsByEmail(customerDto.getEmail());
        if (isUser) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        Customer data = new Customer();

        data.setEmail(customerDto.getEmail());
        data.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        data.setNickname(customerDto.getNickname());
        data.setAddress(customerDto.getAddress());
        data.setPhone(customerDto.getPhone());

        RoleEnum role;
        if (customerDto.getRole()== RoleEnum.OWNER) {
            role = RoleEnum.OWNER;
            data.setAccount(0);
        } else {
            role = RoleEnum.CUSTOMER;
            data.setAccount(1000000);
        }
        data.setRole(role);

        customerRepository.save(data);
    }
}
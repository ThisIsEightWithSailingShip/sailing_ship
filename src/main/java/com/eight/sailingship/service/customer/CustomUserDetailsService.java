package com.eight.sailingship.service.customer;

import com.eight.sailingship.dto.customer.CustomUserDetails;
import com.eight.sailingship.entity.Customer;
import com.eight.sailingship.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final com.eight.sailingship.repository.CustomerRepository CustomerRepository;

    public CustomUserDetailsService(CustomerRepository CustomerRepository) {

        this.CustomerRepository = CustomerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //DB에서 조회
        Customer customerData = CustomerRepository.findByEmail(email);

        if (customerData != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(customerData);
        }

        return null;
    }
}
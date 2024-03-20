package com.eight.sailingship.security;

import com.eight.sailingship.entity.Customer;
import com.eight.sailingship.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("come my");
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("Not Found " + email));

        return new UserDetailsImpl(customer);
    }
}
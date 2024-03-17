package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    boolean existsByEmail(String email);
}

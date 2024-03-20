package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);
}

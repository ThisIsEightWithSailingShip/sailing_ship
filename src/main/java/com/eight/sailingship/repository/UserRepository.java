package com.eight.sailingship.repository;

import com.eight.sailingship.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

//    Optional<User> findById(Long userId);
}

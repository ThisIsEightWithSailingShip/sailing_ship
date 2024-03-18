package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Order;
import com.eight.sailingship.entity.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStatus(StatusEnum statusEnum);
}

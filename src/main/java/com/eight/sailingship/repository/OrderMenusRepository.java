package com.eight.sailingship.repository;

import com.eight.sailingship.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenusRepository extends JpaRepository<OrderMenu,Long> {
}

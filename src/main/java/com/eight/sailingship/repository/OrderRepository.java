package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Order;
import com.eight.sailingship.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

}

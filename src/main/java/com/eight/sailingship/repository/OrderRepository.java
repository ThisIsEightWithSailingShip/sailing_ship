package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Customer;
import com.eight.sailingship.entity.Order;
import com.eight.sailingship.entity.StatusEnum;
import com.eight.sailingship.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStatus(StatusEnum statusEnum);

    Optional<Order> findByCustomerAndStatus(Customer customer, StatusEnum statusEnum);
    List<Order> findByStoreAndStatus(Store store, StatusEnum statusEnum);
    List<Order> findByStore(Store store);
    List<Order> findAllByOrderByOrderDateDesc();


}

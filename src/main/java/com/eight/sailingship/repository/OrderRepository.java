package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Store,Long> {

}

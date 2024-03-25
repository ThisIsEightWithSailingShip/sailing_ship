package com.eight.sailingship.repository;

import com.eight.sailingship.entity.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @EntityGraph(attributePaths = {"store", "imagePhoto"})
    List<Menu> findByStore_StoreId(long id);
}

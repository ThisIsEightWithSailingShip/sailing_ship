package com.eight.sailingship;

import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class SailingshipApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @Transactional
    void contextLoads() {
    }

}

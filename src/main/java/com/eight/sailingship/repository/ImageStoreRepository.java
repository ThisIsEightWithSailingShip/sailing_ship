package com.eight.sailingship.repository;

import com.eight.sailingship.entity.ImageStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageStoreRepository extends JpaRepository <ImageStore, Long> {
}

package com.eight.sailingship.repository;

import com.eight.sailingship.entity.ImagePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImagePhoto, Long> {
}
package com.eight.sailingship.repository;

import com.eight.sailingship.entity.ImagePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImagePhoto, Long> {
    List<ImagePhoto> findByStore_StoreId(long storeId);
    ImagePhoto findByStore_StoreIdAndMenu_MenuId(long storeId, Long menuId);
}
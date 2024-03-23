package com.eight.sailingship.repository;

import com.eight.sailingship.entity.ImagePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImagePhoto, Long> {
    List<ImagePhoto> findByStore_StoreId(long storeId);

    ImagePhoto findByStore_StoreIdAndMenu_MenuId(long storeId, Long menuId);

    @Query("SELECT i FROM image i WHERE i.store.storeId = :storeId")
    List<ImagePhoto> findByStoreId(Long storeId);
}
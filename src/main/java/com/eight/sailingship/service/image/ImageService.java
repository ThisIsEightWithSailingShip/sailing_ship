package com.eight.sailingship.service.image;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.Image.ImageUploadResponseDto;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.ImageRepository;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import com.eight.sailingship.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Autowired
    private S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<ImageUploadResponseDto> saveMenuImage(MultipartFile images, Long menuId, UserDetailsImpl userDetails) throws IOException {

        if(images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImageUploadResponseDto(null, "업로드할 이미지가 없습니다."));
        }

        // 이미지 업로드 로직
        String storedFileName = s3Uploader.upload(images, "image");
        if(storedFileName == null || storedFileName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageUploadResponseDto(null, "이미지 업로드에 실패했습니다."));
        }
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호입니다."));

        ImagePhoto imagePhoto = new ImagePhoto(storedFileName, store);
        menuRepository.findByStore_StoreId(store.getStoreId()).stream()
                .filter(menu -> Objects.equals(menu.getMenuId(), menuId))
                .findFirst()
                .ifPresent(imagePhoto::setMenu);

        ImagePhoto savedImage = imageRepository.save(imagePhoto);
        String responseSentence = "이미지를 성공적으로 업로드 했습니다.";
        ImageUploadResponseDto response = new ImageUploadResponseDto(savedImage.getImageId(), responseSentence);
        return ResponseEntity.ok(response);

    }

    @Transactional(readOnly = true)
    public List<ImagePhoto> listImage(UserDetailsImpl userDetails) {
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호입니다."));
        return imageRepository.findByStore_StoreId(store.getStoreId());
    }

    @Transactional
    public void editImageMenu(MultipartFile images, Long menuId, UserDetailsImpl userDetails) throws IOException {
        if (!images.isEmpty()) {
            Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호입니다."));
            ImagePhoto imagePhoto = imageRepository.findByStore_StoreIdAndMenu_MenuId(store.getStoreId(), menuId);
            Menu menu = menuRepository.findById(menuId).get();

            String storedFileName = s3Uploader.upload(images, "image");
            imagePhoto.setImageUrl(storedFileName);

            imageRepository.save(imagePhoto);
        }
    }


    //매장 이미지
    @Transactional
    public ResponseEntity<ImageUploadResponseDto> saveStoreImage(MultipartFile image, Long storeId) throws IOException {
        ImagePhoto imagePhoto = new ImagePhoto();
        if (!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, "image");
            imagePhoto.setImageUrl(storedFileName);

            Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("매장을 찾을 수 없습니다."));
            imagePhoto.setStore(store);
        }

        ImagePhoto savedImage = imageRepository.save(imagePhoto);
        String responseSentence = "이미지를 성공적으로 업로드 했습니다.";

        ImageUploadResponseDto response = new ImageUploadResponseDto(savedImage.getImageId(), responseSentence);
        return ResponseEntity.ok(response);
    }

    public List<ImagePhoto> listImagesByStoreId(Long storeId) {
        return imageRepository.findByStoreId(storeId);
    }

    @Transactional
    public void updateStoreImage(MultipartFile image, Long storeId) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // 매장 ID로 매장을 조회하여 이미지에 연결합니다.
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("매장을 찾을 수 없습니다."));

        // 해당 매장의 기존 이미지들을 조회

        // 기존 이미지들을 모두 삭제

        // S3에 새 이미지 업로드 후 URL 반환
        String storedFileName = s3Uploader.upload(image, "image");

        // 새 이미지 정보 설정 및 저장
        store.setImageUrl(storedFileName);

    }

    public void saveDefaultImage(Long storeId) {
        String defaultImageUrl = "/images/logo.png";


        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id " + storeId));


        ImagePhoto image = new ImagePhoto(defaultImageUrl, store);


        imageRepository.save(image);
    }



}
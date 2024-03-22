package com.eight.sailingship.service.image;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.Image.ImageUploadResponse;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.ImageRepository;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ImageUploadResponse> saveImage(MultipartFile images, Long itemId, Long userId) throws IOException {
        ImagePhoto imagePhoto = new ImagePhoto();
        if(!images.isEmpty()) {
            String storedFileName = s3Uploader.upload(images, "image");
            imagePhoto.setImageUrl(storedFileName);
        }

        ImagePhoto savedImage = imageRepository.save(imagePhoto);
        String responseSentence = "이미지를 성공적으로 업로드 했습니다.";

        ImageUploadResponse response = new ImageUploadResponse(savedImage.getImageId(), responseSentence);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<ImageUploadResponse> saveMenuImage(MultipartFile images, Long menuId, UserDetailsImpl userDetails) throws IOException {
        ImagePhoto imagePhoto = new ImagePhoto();
        if(!images.isEmpty()) {
            String storedFileName = s3Uploader.upload(images, "image");
            imagePhoto.setImageUrl(storedFileName);
            imagePhoto.setStore(userDetails.getUser().getStore());
            List<Menu> menus = menuRepository.findByStore_StoreId(userDetails.getUser().getStore().getStoreId());
            for (Menu menu : menus) {
                if (Objects.equals(menu.getMenuId(), menuId)) {
                    imagePhoto.setMenu(menu);
                    break;
                }
            }

        }
        ImagePhoto savedImage = imageRepository.save(imagePhoto);
        String responseSentence = "이미지를 성공적으로 업로드 했습니다.";

        ImageUploadResponse response = new ImageUploadResponse(savedImage.getImageId(), responseSentence);
        return ResponseEntity.ok(response);

    }

    public List<ImagePhoto> listImage(UserDetailsImpl userDetails) {
        return imageRepository.findByStore_StoreId(userDetails.getUser().getStore().getStoreId());
    }

    public void editImageMenu(MultipartFile images, Long menuId, UserDetailsImpl userDetails) throws IOException {
        if (!images.isEmpty()) {
            ImagePhoto imagePhoto = imageRepository.findByStore_StoreIdAndMenu_MenuId(userDetails.getUser().getStore().getStoreId(), menuId);
            Menu menu = menuRepository.findById(menuId).get();

            String storedFileName = s3Uploader.upload(images, "image");
            imagePhoto.setImageUrl(storedFileName);

            imageRepository.save(imagePhoto);
        }
    }


    //매장 이미지
    @Transactional
    public ResponseEntity<ImageUploadResponse> saveStoreImage(MultipartFile image, Long storeId) throws IOException {
        ImagePhoto imagePhoto = new ImagePhoto();
        if (!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, "image");
            imagePhoto.setImageUrl(storedFileName);
            // 매장 ID로 매장을 조회하여 이미지에 연결합니다.
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("매장을 찾을 수 없습니다."));
            imagePhoto.setStore(store);
        }
        // 이미지를 저장하고 저장된 이미지 정보를 반환합니다.
        ImagePhoto savedImage = imageRepository.save(imagePhoto);
        String responseSentence = "이미지를 성공적으로 업로드 했습니다.";

        ImageUploadResponse response = new ImageUploadResponse(savedImage.getImageId(), responseSentence);
        return ResponseEntity.ok(response);
    }

    public List<ImagePhoto> listImagesByStoreId(Long storeId) {
        return imageRepository.findByStoreId(storeId);
    }
}

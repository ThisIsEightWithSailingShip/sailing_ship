package com.eight.sailingship.service.image;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.Image.ImageUploadResponseDto;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.repository.ImageRepository;
import com.eight.sailingship.repository.MenuRepository;
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

        ImagePhoto imagePhoto = new ImagePhoto(storedFileName, userDetails.getUser().getStore());
        menuRepository.findByStore_StoreId(userDetails.getUser().getStore().getStoreId()).stream()
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
        return imageRepository.findByStore_StoreId(userDetails.getUser().getStore().getStoreId());
    }

    @Transactional
    public void editImageMenu(MultipartFile images, Long menuId, UserDetailsImpl userDetails) throws IOException {
        if (!images.isEmpty()) {
            ImagePhoto imagePhoto = imageRepository.findByStore_StoreIdAndMenu_MenuId(userDetails.getUser().getStore().getStoreId(), menuId);

            String storedFileName = s3Uploader.upload(images, "image");
            imagePhoto.setImageUrl(storedFileName);

            imageRepository.save(imagePhoto);
        }
    }
}

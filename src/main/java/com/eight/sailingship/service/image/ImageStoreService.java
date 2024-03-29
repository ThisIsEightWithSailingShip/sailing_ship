package com.eight.sailingship.service.image;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.Image.ImageUploadResponseDto;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.entity.ImageStore;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.ImageRepository;
import com.eight.sailingship.repository.ImageStoreRepository;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import com.eight.sailingship.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageStoreService {
    private final StoreRepository storeRepository;
    private final ImageStoreRepository imageStoreRepository;

    @Autowired
    private S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<ImageUploadResponseDto> saveStoreImage(MultipartFile images, Long storeId, UserDetailsImpl userDetails) throws IOException {
        if(images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImageUploadResponseDto(null, "업로드할 이미지가 없습니다."));
        }

        // 이미지 업로드 로직
        String storedFileName = s3Uploader.upload(images, "storeImage");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호입니다."));

        ImageStore imageStore = new ImageStore(storedFileName, store);

        ImageStore savedImage = imageStoreRepository.save(imageStore);
        String responseSentence = "이미지를 성공적으로 업로드 했습니다.";
        ImageUploadResponseDto response = new ImageUploadResponseDto(savedImage.getImageId(), responseSentence);
        return ResponseEntity.ok(response);
    }
}

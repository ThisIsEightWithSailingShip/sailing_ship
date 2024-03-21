package com.eight.sailingship.service.image;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.Image.ImageUploadResponse;
import com.eight.sailingship.entity.ImagePhoto;
import com.eight.sailingship.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

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


}

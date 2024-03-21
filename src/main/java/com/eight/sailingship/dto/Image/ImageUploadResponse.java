package com.eight.sailingship.dto.Image;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUploadResponse {

    @Schema(description = "이미지 아이디", nullable = false, example = "2")
    private Long imageId;

    @Schema(description = "반환 메시지", nullable = false, example = "상품 등록에 성공~!")
    private String responseAnswer;

    public ImageUploadResponse(Long imageId, String responseSentence) {
        this.imageId = imageId;
        this.responseAnswer = responseSentence;
    }
}

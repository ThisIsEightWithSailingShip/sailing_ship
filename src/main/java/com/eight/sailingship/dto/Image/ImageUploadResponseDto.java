package com.eight.sailingship.dto.Image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUploadResponseDto {
    private final Long imageId;
    private final String responseAnswer;

    public ImageUploadResponseDto(Long imageId, String responseSentence) {
        this.imageId = imageId;
        this.responseAnswer = responseSentence;
    }
}

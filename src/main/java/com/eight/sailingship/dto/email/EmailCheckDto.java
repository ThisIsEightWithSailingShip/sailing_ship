package com.eight.sailingship.dto.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailCheckDto {
    @NotEmpty(message = "인증 번호를 입력해 주세요")
    private String authNum;
}

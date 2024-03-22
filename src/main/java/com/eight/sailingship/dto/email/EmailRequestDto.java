package com.eight.sailingship.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {
    @Email
    @NotEmpty(message = "올바른 이메일을 입력해 주세요")
    private String email;
}
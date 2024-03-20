package com.eight.sailingship.dto.user;

import com.eight.sailingship.entity.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSignUpRequestDto {

    private String email;
    private String password;
    private String nickname;
    private String address;
    private String phone;
    private String role;
}
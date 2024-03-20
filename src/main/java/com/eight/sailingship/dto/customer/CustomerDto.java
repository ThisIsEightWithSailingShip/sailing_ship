package com.eight.sailingship.dto.customer;

import com.eight.sailingship.entity.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDto {

    private String email;
    private String password;
    private String nickname;
    private String address;
    private String phone;
    private RoleEnum role;
}
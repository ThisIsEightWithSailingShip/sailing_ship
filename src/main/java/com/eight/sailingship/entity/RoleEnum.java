package com.eight.sailingship.entity;

import lombok.Getter;

@Getter
public enum RoleEnum {
    OWNER(Role.OWNER),
    CUSTOMER(Role.CUSTOMER);

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getAuthority() { return this.role; }

    public static class Role {
        // 권한 이름 규칙은 "ROLE_"로 시작해야 함
        public static final String OWNER = "ROLE_OWNER";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
    }
}

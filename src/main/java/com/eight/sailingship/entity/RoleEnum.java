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

    public static class Role {
        public static final String OWNER = "ROLE_OWNER";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
    }
}

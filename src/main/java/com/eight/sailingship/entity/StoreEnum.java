package com.eight.sailingship.entity;

import lombok.Getter;

@Getter
public enum StoreEnum {
    KOREA(Category.KOREA),
    JAPAN(Category.JAPAN),
    CHINA(Category.CHINA),
    ETC(Category.ETC);

    private final String role;

    StoreEnum(String role) {
        this.role = role;
    }

    public static class Category {
        public static final String KOREA = "KOREA";
        public static final String JAPAN = "JAPAN";
        public static final String CHINA = "CHINA";
        public static final String ETC = "ETC";
    }
}

package com.eight.sailingship.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class StatusResponse {
    private int statusCode;
    private String message;

    public StatusResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

package com.drujba.autobackend.models.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private String timestamp;

    public ErrorResponse() {
        this.timestamp = Instant.now().toString();
    }

    public ErrorResponse(String error, String message, int status) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
    }
}
package com.drujba.autobackend.exceptions.auth;

public class JwtAuthenticationException extends RuntimeException {

    private final int httpStatus;

    public JwtAuthenticationException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}


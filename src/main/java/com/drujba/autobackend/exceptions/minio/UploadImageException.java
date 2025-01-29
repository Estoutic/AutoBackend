package com.drujba.autobackend.exceptions.minio;

public class UploadImageException extends RuntimeException {
    public UploadImageException(Exception e) {
        super(String.format("error occurred while uploading image: %s", e.getMessage()));
    }
}

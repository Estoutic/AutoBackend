package com.drujba.autobackend.exceptions.minio;

public class UploadImageException extends RuntimeException {
    public UploadImageException(Exception e) {
        super(String.format("error occurred while uploading image: %s", e.getMessage()));
    }
    public UploadImageException(String message) {
        super(String.format("error occurred while uploading image: %s", message));
    }

    public UploadImageException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.drujba.autobackend.exceptions.minio;

public class UpdateFileException extends RuntimeException{
    public UpdateFileException(String id, Exception exception) {
        super(String.format("Error updating file %s.", id), exception);
    }
}

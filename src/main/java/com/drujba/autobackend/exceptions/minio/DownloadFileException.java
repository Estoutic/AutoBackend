package com.drujba.autobackend.exceptions.minio;

public class DownloadFileException extends RuntimeException {
    public DownloadFileException(String filePath, Exception exception) {
        super(String.format("Error occurred while downloading file '%s'.", filePath), exception);
    }
}

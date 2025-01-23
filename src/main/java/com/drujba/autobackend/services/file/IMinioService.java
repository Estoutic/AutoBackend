package com.drujba.autobackend.services.file;

import com.drujba.autobackend.models.enums.BucketType;
import lombok.SneakyThrows;

import java.io.InputStream;

public interface IMinioService {

    @SneakyThrows
    String uploadFile(BucketType bucketType, String fileName, InputStream inputStream, String contentType);

    @SneakyThrows
    void deleteFile(BucketType bucketType, String fileName);
}

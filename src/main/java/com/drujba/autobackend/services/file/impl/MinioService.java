package com.drujba.autobackend.services.file.impl;

import com.drujba.autobackend.exceptions.minio.DownloadFileException;
import com.drujba.autobackend.models.enums.BucketType;
import com.drujba.autobackend.services.file.IMinioService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioService implements IMinioService {

    @Value("${minio.buckets.image}")
    private String imageBucketName;

    @Value("${minio.buckets.reports}")
    private String reportsBucketName;

    @Value("${minio.url}")
    private String minioUrl;

    private final MinioClient minioClient;

    @SneakyThrows
    @Override
    public String uploadFile(BucketType bucketType, String fileName, InputStream inputStream, String contentType) {
        String bucketName = getBucketName(bucketType);

        ensureBucketExists(bucketName);

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, -1, 10485760)
                .contentType(contentType)
                .build());

        return String.format("%s/%s/%s", minioUrl, bucketName, fileName);
    }

    @SneakyThrows
    @Override
    public void deleteFile(BucketType bucketType, String fileName) {
        String bucketName = getBucketName(bucketType);

        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @Override
    public ByteArrayInputStream downloadFile(BucketType bucketType, String filePath) {
        try {
            return new ByteArrayInputStream(minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(getBucketName(bucketType))
                            .object(filePath)
                            .build()
            ).readAllBytes());
        } catch (Exception e) {
            throw new DownloadFileException(filePath, e);
        }
    }

    private String getBucketName(BucketType bucketType) {
        return switch (bucketType) {
            case IMAGE -> imageBucketName;
            case REPORT -> reportsBucketName;
            default -> throw new IllegalArgumentException("Unknown bucket type: " + bucketType);
        };
    }

    @SneakyThrows
    private void ensureBucketExists(String bucketName) {
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}
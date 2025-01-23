package com.drujba.autobackend.services.file.impl;

import com.drujba.autobackend.services.file.IMinioService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public String uploadFile(String fileName, InputStream inputStream, String contentType) {
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(imageBucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(imageBucketName).build());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(imageBucketName)
                .object(fileName)
                .stream(inputStream, -1, 10485760)
                .contentType(contentType)
                .build());

        return String.format("%s/%s/", minioUrl, imageBucketName);
    }

//    TODO оптимизировать???

    @SneakyThrows
    @Override
    public String uploadReport(String fileName, InputStream inputStream, String contentType) {
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(reportsBucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(reportsBucketName).build());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(reportsBucketName)
                .object(fileName)
                .stream(inputStream, -1, 10485760)
                .contentType(contentType)
                .build());

        return String.format("%s/%s/", minioUrl, reportsBucketName);
    }

    @Override
    @SneakyThrows
    public void deleteFile(String fileName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(imageBucketName)
                        .object(fileName)
                        .build()
        );
    }
}
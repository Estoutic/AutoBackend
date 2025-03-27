package com.drujba.autobackend.configs;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.buckets.image}")
    private String imageBucket;

    @Value("${minio.buckets.reports}")
    private String reportsBucket;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        try {
            // Настраиваем bucket для изображений
            configureBucket(client, imageBucket);

            // Настраиваем bucket для отчетов
            configureBucket(client, reportsBucket);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    private void configureBucket(MinioClient client, String bucketName) throws Exception {
        // Проверяем существование бакета
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            // Создаем бакет, если не существует
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // Настраиваем CORS политику
        String corsPolicy = "{\n" +
                "    \"Version\": \"2012-10-17\",\n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Effect\": \"Allow\",\n" +
                "            \"Principal\": {\"AWS\": \"*\"},\n" +
                "            \"Action\": [\"s3:GetObject\"],\n" +
                "            \"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        client.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(corsPolicy)
                        .build()
        );
    }
}
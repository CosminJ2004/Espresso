package com.example.backend.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "minio.enabled", havingValue = "true")
public class MinioService {

    private final S3Client s3Client;
    private final String bucketName = "photos"; // înlocuiește cu bucketul tău
    private final String baseUrl = "http://13.61.12.137:9000"; // înlocuiește cu IP-ul tău public MinIO

    public MinioService() {
        Dotenv dotenv = Dotenv.load();
        String accessKey = dotenv.get("MINIO_ACCESS_KEY");
        String secretKey = dotenv.get("MINIO_SECRET_KEY");
        
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(baseUrl))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return baseUrl + "/" + bucketName + "/" + filename;
    }
}

package com.example.backend.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Repository
public class StorageRepository {

    private final S3Client client;
    private final String bucket;

    public StorageRepository(@Value("${storage.url}") String url,
                           @Value("${storage.bucket}") String bucket,
                           @Value("${storage.username}") String username,
                           @Value("${storage.password}") String password,
                           @Value("${storage.region}") String region) {
        this.bucket = bucket;
        this.client = S3Client.builder()
                .endpointOverride(URI.create(url))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(username, password)
                ))
                .region(Region.of(region))
                .forcePathStyle(true)
                .build();
    }

    public void upload(String path, InputStream inputStream, long size, String contentType) throws IOException {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(path)
                    .contentType(contentType)
                    .build();

            client.putObject(putRequest, RequestBody.fromInputStream(inputStream, size));
        } catch (Exception e) {
            throw new IOException("Failed to upload to storage: " + e.getMessage(), e);
        }
    }

    public byte[] download(String path) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(path)
                    .build();

            return client.getObject(getObjectRequest).readAllBytes();
        } catch (Exception e) {
            throw new IOException("Failed to download from storage: " + e.getMessage(), e);
        }
    }

    public void delete(String path) throws IOException {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(path)
                    .build();

            client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new IOException("Failed to delete from storage: " + e.getMessage(), e);
        }
    }
}
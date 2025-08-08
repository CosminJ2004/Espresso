package com.example.backend.config;

import com.example.backend.service.MinioService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public MinioService minioService() throws IOException {
        MinioService mockMinioService = Mockito.mock(MinioService.class);
        Mockito.when(mockMinioService.uploadImage(Mockito.any(MultipartFile.class)))
                .thenReturn("http://test-minio-url/test-image.jpg");
        return mockMinioService;
    }
} 
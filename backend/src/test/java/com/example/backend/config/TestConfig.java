package com.example.backend.config;

import com.example.backend.service.StorageService;
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
    public StorageService storageService() throws IOException {
        StorageService mockStorageService = Mockito.mock(StorageService.class);
//        Mockito.when(mockStorageService.upload("none", "1", Mockito.any(MultipartFile.class)))
//                .thenReturn(void);
        return mockStorageService;
    }
} 
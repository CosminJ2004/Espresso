package com.example.backend.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class ImageFilterService {

    private final RestTemplate restTemplate = new RestTemplate();

    public byte[] applyFilter(byte[] originalImage, String filterType) {
        String url = "http://16.171.148.84/filter?filter=" + filterType;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(originalImage) {
            @Override
            public String getFilename() {
                return "image.jpg"; // nume temporar
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, requestEntity, byte[].class);

        return response.getBody();
    }
}

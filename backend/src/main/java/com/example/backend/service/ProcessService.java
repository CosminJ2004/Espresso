package com.example.backend.service;

import com.example.backend.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ProcessService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final StorageRepository storageRepository;

    @Value("${processor.url}")
    private String processorUrl;

    @Autowired
    public ProcessService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public void processImage(byte[] image, String filterName, String imageId) throws IOException {
        byte[] convertedImage = convertToPNG(image);

        String originalImagePath = buildImagePath("none", imageId);
        storageRepository.upload(originalImagePath, new ByteArrayInputStream(convertedImage),
                convertedImage.length, "image/png");

        if (filterName != null && !filterName.equals("none")) {
            byte[] filteredImage = applyFilter(convertedImage, filterName);

            String filterImagePath = buildImagePath(filterName, imageId);
            storageRepository.upload(filterImagePath, new ByteArrayInputStream(filteredImage),
                    filteredImage.length, "image/png");
        }
    }

    public byte[] applyFilter(byte[] image, String filterName) {
        String url = processorUrl + "?filter=" + filterName;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(image) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        });
        
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        
        return restTemplate.postForEntity(url, request, byte[].class).getBody();
    }

    private String buildImagePath(String filterName, String imageId) {
        return filterName + "/" + imageId + ".png";
    }

    private byte[] convertToPNG(byte[] image) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        BufferedImage newImage = ImageIO.read(bais);
        if (newImage == null) {
            throw new IOException("Unable to read image file");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newImage, "png", baos);
        return baos.toByteArray();
    }
}

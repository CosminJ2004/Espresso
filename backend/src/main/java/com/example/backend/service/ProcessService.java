package com.example.backend.service;

import com.example.backend.repository.StorageRepository;
import com.example.backend.util.logger.Logger;
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
    private final Logger log;

    @Value("${processor.url}")
    private String processorUrl;

    @Autowired
    public ProcessService(StorageRepository storageRepository, Logger log) {
        this.storageRepository = storageRepository;
        this.log = log;
    }

    public void processImage(byte[] image, String filterName, String imageId) throws IOException {
        log.info("Processing image with ID: " + imageId + ", filter: " + filterName + ", size: " + image.length + " bytes");
        
        try {
            byte[] convertedImage = convertToPNG(image);
            log.info("Image converted to PNG, new size: " + convertedImage.length + " bytes");

            String originalImagePath = buildImagePath("none", imageId);
            storageRepository.upload(originalImagePath, new ByteArrayInputStream(convertedImage),
                    convertedImage.length, "image/png");
            log.info("Original image uploaded to: " + originalImagePath);

            if (filterName != null && !filterName.equals("none")) {
                log.info("Applying filter: " + filterName);
                byte[] filteredImage = applyFilter(convertedImage, filterName);
                log.info("Filter applied, filtered image size: " + filteredImage.length + " bytes");

                String filterImagePath = buildImagePath(filterName, imageId);
                storageRepository.upload(filterImagePath, new ByteArrayInputStream(filteredImage),
                        filteredImage.length, "image/png");
                log.info("Filtered image uploaded to: " + filterImagePath);
            }
            
            log.info("Image processing completed successfully for ID: " + imageId);
        } catch (IOException e) {
            log.error("Failed to process image with ID: " + imageId, e);
            throw e;
        }
    }

    public byte[] applyFilter(byte[] image, String filterName) {
        String url = processorUrl + "?filter=" + filterName;
        log.info("Sending image to filter processor: " + url + ", image size: " + image.length + " bytes");
        
        try {
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
            
            byte[] result = restTemplate.postForEntity(url, request, byte[].class).getBody();
            log.info("Filter processing completed, result size: " + (result != null ? result.length : 0) + " bytes");
            return result;
        } catch (Exception e) {
            log.error("Failed to apply filter: " + filterName + " at URL: " + url, e);
            throw e;
        }
    }

    private String buildImagePath(String filterName, String imageId) {
        return filterName + "/" + imageId + ".png";
    }

    private byte[] convertToPNG(byte[] image) throws IOException {
        log.debug("Converting image to PNG format, input size: " + image.length + " bytes");
        
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(image);
            BufferedImage newImage = ImageIO.read(bais);
            if (newImage == null) {
                log.error("Unable to read image file - invalid format or corrupted data");
                throw new IOException("Unable to read image file");
            }

            log.debug("Image loaded successfully, dimensions: " + newImage.getWidth() + "x" + newImage.getHeight());
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImage, "png", baos);
            byte[] result = baos.toByteArray();
            
            log.debug("PNG conversion completed, output size: " + result.length + " bytes");
            return result;
        } catch (IOException e) {
            log.error("Failed to convert image to PNG", e);
            throw e;
        }
    }
}

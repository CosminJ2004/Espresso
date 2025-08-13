package com.example.backend.service;

import com.example.backend.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    @Autowired
    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public void uploadImage(String filter, String imageId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        byte[] pngImageBytes = convertToPng(file);
        String url = buildImageUrl(filter, imageId);
        
        storageRepository.upload(url, new ByteArrayInputStream(pngImageBytes),
                               pngImageBytes.length, "image/png");
    }

    public byte[] downloadImage(String filter, String imageId) throws IOException {
        String url = buildImageUrl(filter, imageId);
        return storageRepository.download(url);
    }

    private String buildImageUrl(String filter, String imageId) {
        return filter + "/" + imageId + ".png";
    }

    private byte[] convertToPng(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("Unable to read image file");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}

package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path rootPath = Paths.get(uploadDir);

        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }

        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase() : "";

        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Äá»nh dáº¡ng file khÃ´ng há»£p lá» (chá» nháº­n jpg, png, webp).");
        }

        String fileName = UUID.randomUUID().toString() + "." + ext;

        Files.copy(file.getInputStream(), rootPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public void deleteImage(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Path rootPath = Paths.get(uploadDir);
            Files.deleteIfExists(rootPath.resolve(fileName));
        } catch (IOException e) {
            System.err.println("Lá»i khi xÃ³a file: " + fileName);
        }
    }
}

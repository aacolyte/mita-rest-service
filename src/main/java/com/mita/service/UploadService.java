package com.mita.service;

import com.mita.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UploadService {

    private final Path root = Paths.get("posters").toAbsolutePath().normalize();

    private static final Logger log = LoggerFactory.getLogger(UploadService.class);


    public String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileStorageException("Only image files are allowed");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = root.resolve(fileName);

        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        log.info("Uploaded poster {}", fileName);
        return fileName;
    }


    public Resource getPoster(String filename) throws IOException {
        Path filePath = root.resolve(filename).normalize();

        if (!filePath.startsWith(root)) {
            throw new FileStorageException("Invalid file path");
        }

        if (!Files.exists(filePath)) {
            return null;
        }
        return new UrlResource(filePath.toUri());
    }

    public String getContentType(String filename) throws IOException {
        Path filePath = root.resolve(filename).normalize();

        if (!filePath.startsWith(root)) {
            throw new FileStorageException("Invalid file path");
        }

        if (!Files.exists(filePath)) {
            return null;
        }

        return Files.probeContentType(filePath);
    }


    public void deletePosterIfExists(String poster){

        if(poster == null || poster.isBlank()) return;
        try{
            Path filePath = root.resolve(poster).normalize();
            if(!filePath.startsWith(root)){
                return;
            }

            Files.deleteIfExists(filePath);

            log.info("Deleted poster {}", poster);

        } catch (IOException e) {
            log.warn("Failed to delete poster {}", poster, e);
        }
    }





}

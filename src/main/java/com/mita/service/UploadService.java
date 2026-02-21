package com.mita.service;

import com.mita.repository.ItemRepository;
import jakarta.transaction.Transactional;
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
@Transactional
public class UploadService {

    private final ItemRepository itemRepository;

    public UploadService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("posters/").resolve(fileName);

        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }


    public Resource getPoster(String filename) throws IOException {
        Path path = Paths.get("posters").resolve(filename);
        return new UrlResource(path.toUri());
    }

    public String getContentType(String filename) throws IOException {
        Path path = Paths.get("posters").resolve(filename);
        return Files.probeContentType(path);
    }


    public void deletePosterIfExists(String poster){

        if(poster == null || poster.isBlank()) return;
        try{
            Path filePath = Paths.get("posters").toAbsolutePath().resolve(poster).normalize();
            if(!filePath.startsWith(filePath)){
                return;
            }
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete poster: " + poster);
        }
    }





}

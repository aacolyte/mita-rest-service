package com.mita.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        if(file.isEmpty()){
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

    @GetMapping("/posters/{filename}")
    public ResponseEntity<Resource> getPoster(@PathVariable String filename) throws IOException {
        Path path = Paths.get("posters").resolve(filename);
        Resource resource = new UrlResource(path.toUri());

        String contentType = Files.probeContentType(path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }




}

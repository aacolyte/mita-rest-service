package com.mita.controller;

import com.mita.service.UploadService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        return uploadService.upload(file);
    }

    @GetMapping("/posters/{filename}")
    public ResponseEntity<Resource> getPoster(@PathVariable String filename) throws IOException {
        Resource resource = uploadService.getPoster(filename);
        String contentType = uploadService.getContentType(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }




}

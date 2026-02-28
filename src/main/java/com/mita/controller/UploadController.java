package com.mita.controller;

import com.mita.service.UploadService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadService.upload(file));
    }

    @GetMapping("/posters/{filename}")
    public ResponseEntity<Resource> getPoster(@PathVariable String filename) throws IOException {
        Resource resource = uploadService.getPoster(filename);
        String contentType = uploadService.getContentType(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping("/posters/delete")
    public ResponseEntity<Void> deletePoster(@RequestBody Map<String, String> body){
        String poster = body.get("poster");
        uploadService.deletePosterIfExists(poster);

        return ResponseEntity.noContent().build();
    }



}

package com.proyecto.integrador.controller;

import com.proyecto.integrador.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/")
public class S3Controller {

    private S3Service amazonClient;

    @Autowired
    S3Controller(S3Service amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return ResponseEntity.ok(this.amazonClient.uploadFile(file));
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestPart(value = "url") String fileUrl) {
        return ResponseEntity.ok(this.amazonClient.deleteFileFromS3Bucket(fileUrl));
    }
}
package com.proyecto.integrador.controller;

import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class imageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/update")
    public ResponseEntity<Image> updateImage(@RequestPart("id")Long id, @RequestPart("image") MultipartFile image){
        return ResponseEntity.ok(this.imageService.updateImage(id,image));
    }
}

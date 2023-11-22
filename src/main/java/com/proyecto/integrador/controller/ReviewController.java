package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestPart("id") Long id, @RequestPart("review")ReviewDto reviewDto){
        return ResponseEntity.ok(reviewService.createReview(id, reviewDto));
    }

}

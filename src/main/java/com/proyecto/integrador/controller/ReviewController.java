package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Review>> getAllReviewsUser(@RequestParam("id") Long id){
        return ResponseEntity.ok(reviewService.getAllReviewsUser(id));
    }

    @GetMapping("/instrument")
    public ResponseEntity<List<Review>> getAllReviewsInstrument(@RequestParam("id") Long id){
        return ResponseEntity.ok(reviewService.getAllReviewsInstrument(id));
    }

}

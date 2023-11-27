package com.proyecto.integrador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.config.JwtUtil;

import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtUtil jwtUtil;


    @Test
    void createReview() throws Exception {
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getAllReviewsUser() throws Exception {


        when(reviewService.getAllReviewsUser(any(Long.class))).thenReturn(new ArrayList<>());


        RequestBuilder requestBuilder = get("/reviews/user")
                .param("id", "1")
                .with(csrf());


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getAllReviewsInstrument() throws Exception {


        when(reviewService.getAllReviewsInstrument(any(Long.class))).thenReturn(new ArrayList<>());


        RequestBuilder requestBuilder = get("/reviews/instrument")
                .param("id", "1")
                .with(csrf());


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
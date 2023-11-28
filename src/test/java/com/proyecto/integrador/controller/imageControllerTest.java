package com.proyecto.integrador.controller;


import org.junit.jupiter.api.Test;

import com.proyecto.integrador.service.ImageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import org.junit.jupiter.api.BeforeEach;



@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @InjectMocks
    private imageController imageController;

    @Mock
    private ImageService imageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void updateImage() throws Exception {
        //error 405
    }



}
package com.proyecto.integrador.controller;


import com.proyecto.integrador.entity.Image;
import org.junit.jupiter.api.Test;

import com.proyecto.integrador.service.ImageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


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
        Long id = 1L;
        MultipartFile image = new MockMultipartFile("image", "hello.png", "image/png", "some image".getBytes());

        Image expectedImage = new Image();
        when(imageService.updateImage(id, image)).thenReturn(expectedImage);

        ResponseEntity<Image> response = imageController.updateImage(id, image);

        assertEquals(ResponseEntity.ok(expectedImage), response);
    }



}
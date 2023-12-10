package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createImage() {
        MultipartFile mockImageFile = mock(MultipartFile.class);


        when(s3Service.uploadFile(mockImageFile)).thenReturn("url_de_la_imagen");

        Image savedImage = new Image();
        savedImage.setId(1L);
        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);


        Image resultImage = imageService.createImage(mockImageFile);


        verify(s3Service, times(1)).uploadFile(mockImageFile);
        verify(imageRepository, times(2)).save(any(Image.class)); //TODO: ask why it saves the method twice


        assertNotNull(resultImage);
        assertEquals(1L, resultImage.getId().longValue());
    }

    @Test
    void createAllImages() {
        List<MultipartFile> mockImages = new ArrayList<>();
        mockImages.add(mock(MultipartFile.class));
        mockImages.add(mock(MultipartFile.class));


        when(s3Service.uploadFile(any(MultipartFile.class))).thenReturn("url_de_la_imagen");
        when(imageRepository.saveAll(anyList())).thenReturn(new ArrayList<>());


        imageService.createAllImages(mockImages);


        verify(s3Service, times(mockImages.size())).uploadFile(any(MultipartFile.class));
        verify(imageRepository, times(1)).saveAll(anyList());
    }

    @Test
    void updateImage() {
        Long imageId = 1L;
        MultipartFile mockImageFile = mock(MultipartFile.class);

        Image mockImage = new Image();
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(mockImage));
        doReturn("Successfully deleted").when(s3Service).deleteFileFromS3Bucket(anyString());
        when(s3Service.uploadFile(mockImageFile)).thenReturn("url_de_la_imagen");
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);


        imageService.updateImage(imageId, mockImageFile);


        verify(imageRepository, times(1)).findById(imageId);
        verify(s3Service, times(1)).uploadFile(mockImageFile);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void deleteImage() {

        Long imageId = 1L;
        Image mockImage = new Image();
        mockImage.setId(imageId);
        mockImage.setImage("url_de_la_imagen");
        mockImage.setDeleted(false);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(mockImage));
        doReturn("Successfully deleted").when(s3Service).deleteFileFromS3Bucket(anyString());

        assertDoesNotThrow(() -> imageService.deleteImage(imageId));

        verify(imageRepository, times(1)).findById(imageId);
        verify(s3Service, times(1)).deleteFileFromS3Bucket(any());

    }

}
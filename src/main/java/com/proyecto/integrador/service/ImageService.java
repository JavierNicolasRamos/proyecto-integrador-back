package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.exception.ImageSaveException;
import com.proyecto.integrador.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Service s3Service;

    @Transactional
    public Image createImage(MultipartFile imageFile){
        try {
            Image image = new Image();
            image.setImage(this.s3Service.uploadFile(imageFile));
            image.setDeleted(false);
            this.imageRepository.save(image);
            return this.imageRepository.save(image);
        }
        catch (ImageSaveException e){
            throw new ImageSaveException("Error al crear la imagen", e);
        }
    }

    @Transactional
    public List<Image> createAllImages(List<MultipartFile> images){
        try {
            List<Image> savedImages = images.stream()
                    .map(imageMultipartFile -> this.createImage(imageMultipartFile))
                    .collect(Collectors.toList());
            return this.imageRepository.saveAll(savedImages);
        }
        catch (ImageSaveException e){
            throw new ImageSaveException("Error al crear la imagen", e);
        }
    }

    @Transactional
    public Image updateImage(Long id, MultipartFile image) {
        try {
            Optional<Image> imageOptional = imageRepository.findById(id);
            Image updateImage = imageOptional.get();
            this.s3Service.deleteFileFromS3Bucket(updateImage.getImage());
            updateImage.setImage(this.s3Service.uploadFile(image));
            this.imageRepository.save(updateImage);
            logger.info("imagen actualizada con éxito.  ID: " + updateImage.getId());
            return updateImage;
        }
        catch (ImageSaveException e){
            throw new ImageSaveException("Error al guardar la imagen", e);
        }
    }

    @Transactional
    public void deleteImage(Long id){
        try {
            Optional<Image> imageOptional = imageRepository.findById(id);
            Image deleteImage = imageOptional.get();
            deleteImage.setDeleted(true);
            this.s3Service.deleteFileFromS3Bucket(deleteImage.getImage());
            logger.info("imagen eliminada con éxito del s3.");
        }
        catch (ImageSaveException e){
            throw new ImageSaveException("Error al eliminar la imagen", e);
        }
    }

    @Transactional
    public void deleteAllImages(List<Long> ids){
        for (int i = 0; i < ids.size(); i++) {
            this.deleteImage(ids.get(i));
        }
    }
}

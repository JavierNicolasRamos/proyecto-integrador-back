package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.ImageDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.ImageSaveException;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.ImageRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Service s3Service;

    @Transactional
    public void saveImagesInstrument(@NotNull Instrument instrument, List<ImageDto> imageDtos) {
        logger.info("Iniciando el proceso de guardar imágenes del instrumento con ID: " + instrument.getId());
        try {
            List<Image> savedImages = imageDtos.stream()
                    .map(imageDto -> {
                        Image image = new Image();
                        image.setImage(this.s3Service.uploadFile(imageDto.getImage()));
                        return image;
                    })
                    .collect(Collectors.toList());
            this.imageRepository.saveAll(savedImages);

            instrument.setImage(savedImages);
            this.instrumentRepository.save(instrument);

            logger.info("Guardado de imágenes del instrumento completado con éxito. Instrumento ID: " + instrument.getId());
        } catch (ImageSaveException e) {
            logger.error("Error al guardar las imágenes del instrumento: " + e.getMessage());
            throw new ImageSaveException("Error al guardar las imágenes del instrumento", e);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar las imágenes del instrumento: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void updateImagesInstrument(@NotNull Instrument instrument, List<ImageDto> imageDtos) {
        logger.info("Iniciando el proceso de actualizar imágenes del instrumento con ID: " + instrument.getId());
        try {
            List<Image> savedImages = new ArrayList<>();

            for (ImageDto imageDto : imageDtos) {
                if (imageDto.getId() != null && imageDto.getDeleted()) {
                    Optional<Image> deleteImage = imageRepository.findById(imageDto.getId());
                    if (deleteImage.isPresent()) {
                        Image deletedImage = deleteImage.get();
                        deletedImage.setDeleted(true);
                        this.s3Service.deleteFileFromS3Bucket(deletedImage.getImage());
                        imageRepository.save(deletedImage);
                    }
                } else {
                    Image image = new Image();
                    MultipartFile newImage = imageDto.getImage();

                    if (imageDto.getId() != null) {
                        Optional<Image> imageOptional = imageRepository.findById(imageDto.getId());
                        if (imageOptional.isPresent()) {
                            Image existingImage = imageOptional.get();
                            this.s3Service.deleteFileFromS3Bucket(existingImage.getImage());
                            existingImage.setImage(this.s3Service.uploadFile(newImage));
                            imageRepository.save(existingImage);
                            savedImages.add(existingImage);
                        }
                    } else {
                        image.setImage(this.s3Service.uploadFile(newImage));
                        imageRepository.save(image);
                        savedImages.add(image);
                    }
                }
            }

            instrument.setImage(savedImages);
            this.instrumentRepository.save(instrument);
            logger.info("Actualización de imágenes del instrumento completada con éxito. Instrumento ID: " + instrument.getId());
        } catch (ImageSaveException e) {
            logger.error("Error al actualizar las imágenes del instrumento: " + e.getMessage());
            throw new ImageSaveException("Error al actualizar las imágenes del instrumento", e);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar las imágenes del instrumento: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void saveImageCategory(@NotNull Category category, MultipartFile imageMultipartFile) {
        logger.info("Iniciando el proceso de guardo de imagen de categoría con ID:" + category.getId());
        try {
            Image image = new Image();
            image.setImage(this.s3Service.uploadFile(imageMultipartFile));
            this.imageRepository.save(image);

            Image savedImage = imageRepository.save(image);
            category.setImage(savedImage);

            logger.info("Guardado de imagen de la categoría completado con éxito. Categoria ID: " + category.getId());
        } catch (ImageSaveException e) {
            logger.error("Error al guardar la imagen de la categoria: " + e.getMessage());
            throw new ImageSaveException("Error al guardar la imagen de la categoría", e);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar la imagen de la categoría: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteImageCategory(Category category) {
        try {
            Optional<Image> deleteImage = imageRepository.findById(category.getImage().getId());

            if (deleteImage.isPresent()) {
                Image deletedImage = deleteImage.get();
                deletedImage.setDeleted(true);
                this.s3Service.deleteFileFromS3Bucket(deletedImage.getImage());
                imageRepository.save(deletedImage);
            }
            if (deleteImage.isEmpty()) {
                logger.error("Error al eliminar la imagen de la categoría, el id de la imagen no existe");
                throw new ImageSaveException("Error al eliminar la imagen de la categoría, el id de la imagen es null");
            }
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la imagen de la categoría: " + e.getMessage(), e);
            throw e; //TODO: Agregar excepción personalizada
        }
    }
    @Transactional
    public void updateImageCategory(@NotNull Category category, @NotNull ImageDto imageDto) {
        logger.info("Iniciando el proceso de actualizacion de imagen de categoría con ID:" + category.getId());
        try {
            Image imageSaved = new Image();

            if (imageDto.getId() != null && imageDto.getDeleted()) {
                Optional<Image> deleteImage = imageRepository.findById(imageDto.getId());
                if (deleteImage.isPresent()) {
                    Image DeletedImage = deleteImage.get();
                    DeletedImage.setDeleted(true);

                    this.s3Service.deleteFileFromS3Bucket(DeletedImage.getImage());
                    imageRepository.save(DeletedImage);
                    logger.info("Vieja imagen de la categoría eliminada con éxito. Categoria ID: " + category.getId());
                }
            } else {
                logger.info("Guardando nueva imagen de la categoría con ID: " + category.getId());
                MultipartFile newImage = imageDto.getImage();

                if (imageDto.getId() != null) {
                    Optional<Image> imageOptional = imageRepository.findById(imageDto.getId());
                    if (imageOptional.isPresent()) {
                        Image ExistingImage = imageOptional.get();
                        this.s3Service.deleteFileFromS3Bucket(ExistingImage.getImage());
                        ExistingImage.setImage(this.s3Service.uploadFile(newImage));
                        imageRepository.save(ExistingImage);
                        imageSaved = ExistingImage;
                        logger.info("Imagen de la categoría actualizada con éxito. Categoria ID: " + category.getId());
                    }
                    else {
                        logger.error("Error al guardar la imagen de la categoría, el id de la imagen no existe");
                        throw new ImageSaveException("Error al guardar la imagen de la categoría, el id de la imagen es null");
                    }
                }
            }
            category.setImage(imageSaved);
            logger.info("Guardado de imagen de la categoría completado con éxito. Categoria ID: " + category.getId());
        } catch (ImageSaveException e) {
            logger.error("Error al guardar la imagen de la categoria: " + e.getMessage());
            throw new ImageSaveException("Error al guardar la imagen de la categoría", e);
        }
    }
}

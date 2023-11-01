package com.proyecto.integrador.config;

import com.proyecto.integrador.config.ImageFormatAndSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class ImageFormatAndSizeValidator implements ConstraintValidator<ImageFormatAndSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(ImageFormatAndSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }


    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        if (file.getSize() > maxSize) {
            return false;
        }

        String contentType = file.getContentType();
        if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"))) {
            return true;
        }

        return false;
    }
}
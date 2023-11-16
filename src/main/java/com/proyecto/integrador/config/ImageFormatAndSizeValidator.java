package com.proyecto.integrador.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;


public class ImageFormatAndSizeValidator implements ConstraintValidator<ImageFormatAndSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(@NotNull ImageFormatAndSize constraintAnnotation) {
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
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"));
    }
}
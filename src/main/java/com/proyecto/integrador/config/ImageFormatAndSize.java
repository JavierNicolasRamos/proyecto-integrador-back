package com.proyecto.integrador.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFormatAndSizeValidator.class)
@Documented
public @interface ImageFormatAndSize {
    String message() default "La imagen no cumple con los requisitos.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long maxSize() default Long.MAX_VALUE;
}
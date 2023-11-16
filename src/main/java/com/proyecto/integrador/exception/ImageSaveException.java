package com.proyecto.integrador.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ImageSaveException extends RuntimeException {

    private HttpStatus httpStatus;
    public ImageSaveException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ImageSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

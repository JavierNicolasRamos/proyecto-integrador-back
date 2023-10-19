package com.proyecto.integrador.exception;

public class ImagenGuardadoException extends RuntimeException {
    public ImagenGuardadoException(String message) {
        super(message);
    }

    public ImagenGuardadoException(String message, Throwable cause) {
        super(message, cause);
    }
}

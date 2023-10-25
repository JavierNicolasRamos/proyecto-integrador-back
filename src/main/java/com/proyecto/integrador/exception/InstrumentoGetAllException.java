package com.proyecto.integrador.exception;

public class InstrumentoGetAllException extends RuntimeException {
    public InstrumentoGetAllException(String message) {
        super(message);
    }

    public InstrumentoGetAllException(String message, Throwable cause) {
        super(message, cause);
    }
}

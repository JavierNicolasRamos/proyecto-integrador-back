package com.proyecto.integrador.exception;

public class InstrumentoNotFoundException extends RuntimeException {
    public InstrumentoNotFoundException(String message) {
        super(message);
    }
}

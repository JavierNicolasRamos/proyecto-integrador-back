package com.proyecto.integrador.exception;

public class NonExistentInstrumentException extends RuntimeException {

    public NonExistentInstrumentException(String message) {
        super(message);
    }

    public NonExistentInstrumentException(String message, Throwable cause) {
        super(message, cause);
    }
}

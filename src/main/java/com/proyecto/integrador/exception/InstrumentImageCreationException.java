package com.proyecto.integrador.exception;

public class InstrumentImageCreationException extends RuntimeException {

    public InstrumentImageCreationException(String message) {
        super(message);
    }

    public InstrumentImageCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

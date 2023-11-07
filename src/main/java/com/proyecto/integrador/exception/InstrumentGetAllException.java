package com.proyecto.integrador.exception;

public class InstrumentGetAllException extends RuntimeException {
    public InstrumentGetAllException(String message) {
        super(message);
    }

    public InstrumentGetAllException(String message, Throwable cause) {
        super(message, cause);
    }
}

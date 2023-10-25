package com.proyecto.integrador.exception;

public class ObtenerReservaException extends RuntimeException {
    public ObtenerReservaException(String message) {
        super(message);
    }

    public ObtenerReservaException(String message, Throwable cause) {
        super(message, cause);
    }
}


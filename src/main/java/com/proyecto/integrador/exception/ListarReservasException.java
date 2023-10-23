package com.proyecto.integrador.exception;

public class ListarReservasException extends RuntimeException {
    public ListarReservasException(String message) {
        super(message);
    }

    public ListarReservasException(String message, Throwable cause) {
        super(message, cause);
    }
}


package com.proyecto.integrador.exception;

public class GetReserveException extends RuntimeException {
    public GetReserveException(String message) {
        super(message);
    }

    public GetReserveException(String message, Throwable cause) {
        super(message, cause);
    }
}


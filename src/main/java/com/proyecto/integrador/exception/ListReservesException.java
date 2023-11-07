package com.proyecto.integrador.exception;

public class ListReservesException extends RuntimeException {
    public ListReservesException(String message) {
        super(message);
    }

    public ListReservesException(String message, Throwable cause) {
        super(message, cause);
    }
}


package com.proyecto.integrador.exception;

public class EditBookingException extends RuntimeException {
    public EditBookingException(String message, Throwable e) {
        super(message, e);
    }
}

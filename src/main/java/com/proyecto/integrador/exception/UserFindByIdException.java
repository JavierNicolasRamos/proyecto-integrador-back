package com.proyecto.integrador.exception;

public class UserFindByIdException extends RuntimeException {
    public UserFindByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

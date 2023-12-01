package com.proyecto.integrador.exception;

public class UserFindAllException extends RuntimeException{
    public UserFindAllException() {
        super();
    }

    public UserFindAllException(String message) {
        super(message);
    }

    public UserFindAllException(String message, Throwable cause) {
        super(message, cause);
    }
}

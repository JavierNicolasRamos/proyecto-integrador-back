package com.proyecto.integrador.exception;

public class UserRegisterException extends RuntimeException {
    public UserRegisterException() {
        super();
    }

    public UserRegisterException(String message) {
        super(message);
    }

    public UserRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}

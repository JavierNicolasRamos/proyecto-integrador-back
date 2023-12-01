package com.proyecto.integrador.exception;

public class UserRegisterByEmailException extends RuntimeException {
    public UserRegisterByEmailException() {
        super();
    }

    public UserRegisterByEmailException(String message) {
        super(message);
    }

    public UserRegisterByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

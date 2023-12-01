package com.proyecto.integrador.exception;

public class UserGetNameByEmailException extends RuntimeException {
    public UserGetNameByEmailException() {
        super();
    }

    public UserGetNameByEmailException(String message) {
        super(message);
    }

    public UserGetNameByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.proyecto.integrador.exception;

public class UserGetLastNameByEmailException extends RuntimeException {
    public UserGetLastNameByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

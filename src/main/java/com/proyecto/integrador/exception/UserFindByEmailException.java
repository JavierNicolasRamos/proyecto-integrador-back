package com.proyecto.integrador.exception;

public class UserFindByEmailException extends RuntimeException{

    public UserFindByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

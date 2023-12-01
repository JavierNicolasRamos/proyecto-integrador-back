package com.proyecto.integrador.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(){

    }

    public UserNotFoundException(String message, Throwable e){
        super(message, e);
    }
}

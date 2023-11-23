package com.proyecto.integrador.exception;

public class UserValidationException extends RuntimeException{
    public UserValidationException(){
    }

    public UserValidationException(String message){
        super(message);
    }
    public UserValidationException(String message, Throwable throwable){
        super(message, throwable);
    }
}

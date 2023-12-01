package com.proyecto.integrador.exception;

public class CreateBookingException extends RuntimeException{
    public CreateBookingException(String message, Throwable e){
        super(message, e);
    }
}

package com.proyecto.integrador.exception;

public class NonExistentReservaException extends RuntimeException {

    public NonExistentReservaException(String message){super(message);}

    public NonExistentReservaException(String message, Throwable cause){super(message, cause);}

}

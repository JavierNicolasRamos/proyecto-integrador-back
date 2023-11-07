package com.proyecto.integrador.exception;

public class NonExistentReserveException extends RuntimeException {

    public NonExistentReserveException(String message){super(message);}

    public NonExistentReserveException(String message, Throwable cause){super(message, cause);}

}

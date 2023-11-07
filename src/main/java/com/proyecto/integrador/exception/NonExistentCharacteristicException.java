package com.proyecto.integrador.exception;

public class NonExistentCharacteristicException extends RuntimeException{
    public NonExistentCharacteristicException(String message) {
        super(message);
    }

    public NonExistentCharacteristicException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.proyecto.integrador.exception;

public class NonExistentCharacteristicExcpetion extends RuntimeException{
    public NonExistentCharacteristicExcpetion(String message) {
        super(message);
    }

    public NonExistentCharacteristicExcpetion(String message, Throwable cause) {
        super(message, cause);
    }
}

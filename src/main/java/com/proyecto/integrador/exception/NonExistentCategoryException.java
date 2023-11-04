package com.proyecto.integrador.exception;

public class NonExistentCategoryException extends RuntimeException {
    public NonExistentCategoryException(String message){
        super(message);
    }
}

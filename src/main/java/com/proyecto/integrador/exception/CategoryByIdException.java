package com.proyecto.integrador.exception;

public class CategoryByIdException extends RuntimeException{
    public CategoryByIdException(String message, Throwable e){
        super(message, e);
    }
}

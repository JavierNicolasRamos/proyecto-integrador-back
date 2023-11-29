package com.proyecto.integrador.exception;

public class CategoryByNameException extends RuntimeException{
    public CategoryByNameException(String message, Throwable e){
        super(message, e);
    }


}

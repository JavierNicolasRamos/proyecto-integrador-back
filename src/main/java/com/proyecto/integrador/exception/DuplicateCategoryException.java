package com.proyecto.integrador.exception;

public class DuplicateCategoryException extends RuntimeException{
    public DuplicateCategoryException(String message){
        super(message);
    }
}

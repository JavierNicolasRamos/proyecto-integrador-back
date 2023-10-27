package com.proyecto.integrador.exception;

public class DuplicateCategoriaException extends RuntimeException{
    public DuplicateCategoriaException(String message){
        super(message);
    }
}

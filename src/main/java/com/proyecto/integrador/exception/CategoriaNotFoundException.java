package com.proyecto.integrador.exception;

public class CategoriaNotFoundException extends RuntimeException{

    public CategoriaNotFoundException(String message){
        super(message);
    }
}

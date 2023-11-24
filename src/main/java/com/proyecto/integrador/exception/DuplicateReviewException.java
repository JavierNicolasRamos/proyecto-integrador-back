package com.proyecto.integrador.exception;

public class DuplicateReviewException extends RuntimeException{

    public DuplicateReviewException(String message){
        super(message);
    }

    public DuplicateReviewException(){
    }
}

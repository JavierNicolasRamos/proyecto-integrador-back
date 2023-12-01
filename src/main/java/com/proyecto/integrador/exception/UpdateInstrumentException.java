package com.proyecto.integrador.exception;


public class UpdateInstrumentException extends RuntimeException {
    public  UpdateInstrumentException(String message, Throwable e){
        super(message, e);
    }
}

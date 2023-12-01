package com.proyecto.integrador.exception;

public class UpdateUserByIdException extends  RuntimeException{
    public UpdateUserByIdException() {
        super();
    }

    public UpdateUserByIdException(String message) {
        super(message);
    }

    public UpdateUserByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

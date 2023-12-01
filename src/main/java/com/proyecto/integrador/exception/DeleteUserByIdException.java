package com.proyecto.integrador.exception;

public class DeleteUserByIdException extends RuntimeException{
    public DeleteUserByIdException() {
        super();
    }

    public DeleteUserByIdException(String message) {
        super(message);
    }

    public DeleteUserByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

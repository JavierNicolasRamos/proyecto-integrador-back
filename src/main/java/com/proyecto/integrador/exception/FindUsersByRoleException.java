package com.proyecto.integrador.exception;

public class FindUsersByRoleException extends  RuntimeException{
    public FindUsersByRoleException() {
        super();
    }

    public FindUsersByRoleException(String message) {
        super(message);
    }

    public FindUsersByRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}

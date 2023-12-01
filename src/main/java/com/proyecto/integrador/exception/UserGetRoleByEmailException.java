package com.proyecto.integrador.exception;

public class UserGetRoleByEmailException extends RuntimeException{
    public UserGetRoleByEmailException() {
        super();
    }

    public UserGetRoleByEmailException(String message) {
        super(message);
    }

    public UserGetRoleByEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}

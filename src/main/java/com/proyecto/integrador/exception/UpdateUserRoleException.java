package com.proyecto.integrador.exception;

public class UpdateUserRoleException extends RuntimeException {
    public UpdateUserRoleException() {
        super();
    }

    public UpdateUserRoleException(String message) {
        super(message);
    }

    public UpdateUserRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}

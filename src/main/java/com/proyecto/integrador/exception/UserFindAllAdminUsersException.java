package com.proyecto.integrador.exception;

public class UserFindAllAdminUsersException extends RuntimeException {
    public UserFindAllAdminUsersException() {
        super();
    }

    public UserFindAllAdminUsersException(String message) {
        super(message);
    }

    public UserFindAllAdminUsersException(String message, Throwable cause) {
        super(message, cause);
    }
}

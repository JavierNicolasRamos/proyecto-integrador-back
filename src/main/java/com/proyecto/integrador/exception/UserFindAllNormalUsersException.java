package com.proyecto.integrador.exception;

public class UserFindAllNormalUsersException extends RuntimeException{
    public UserFindAllNormalUsersException() {
        super();
    }

    public UserFindAllNormalUsersException(String message) {
        super(message);
    }

    public UserFindAllNormalUsersException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.drujba.autobackend.exceptions.auth;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(String email) {
        super(String.format("User does not exist %s", email));
    }
}

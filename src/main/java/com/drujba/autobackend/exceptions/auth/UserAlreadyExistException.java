package com.drujba.autobackend.exceptions.auth;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String userId) {
        super(String.format("User already exist %s", userId));
    }
}

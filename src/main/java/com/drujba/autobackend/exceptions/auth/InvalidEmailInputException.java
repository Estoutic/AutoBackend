package com.drujba.autobackend.exceptions.auth;

public class InvalidEmailInputException extends RuntimeException{
    public InvalidEmailInputException(String email) {
        super(String.format("Invalid Email %s", email));

    }
}

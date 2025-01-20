package com.drujba.autobackend.exceptions.application;

public class ApplicationDoesNotExistException extends RuntimeException {
    public ApplicationDoesNotExistException(String message) {
        super(String.format("Application does not exist: %s", message));
    }
}

package com.drujba.autobackend.exceptions.car;

public class CarDoesNotExistException extends RuntimeException {
    public CarDoesNotExistException(String message) {
        super("Car does not exist with id: " + message);
    }
}

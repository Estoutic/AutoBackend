package com.drujba.autobackend.exceptions.car;

public class CarModelDoesNotExistException extends RuntimeException {
    public CarModelDoesNotExistException(String message) {
        super(String.format("Car model %s does not exist", message));
    }
}

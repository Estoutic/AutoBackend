package com.drujba.autobackend.exceptions.car;

public class CarModelAlreadyExistException extends RuntimeException {
    public CarModelAlreadyExistException() {
        super("Car model with the same brand, model, and generation already exists");
    }
}

package com.drujba.autobackend.exceptions.car;

public class CarTranslationDoesNotExistException extends RuntimeException{
    public CarTranslationDoesNotExistException(String id) {
        super("Car translation with id " + id + " does not exist");
    }
}

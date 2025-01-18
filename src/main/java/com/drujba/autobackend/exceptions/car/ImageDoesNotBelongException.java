package com.drujba.autobackend.exceptions.car;

public class ImageDoesNotBelongException extends RuntimeException{
    public ImageDoesNotBelongException(String message) {
        super(String.format("Image %s does not belong to the car", message));
    }
}

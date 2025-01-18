package com.drujba.autobackend.exceptions.car;

public class ImageDoestNotExistException extends RuntimeException{
    public ImageDoestNotExistException(String message) {
        super(String.format("Image %s does not exist", message));
    }
}

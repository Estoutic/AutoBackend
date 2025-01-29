package com.drujba.autobackend.exceptions.application;

public class StatusAlreadySetException extends RuntimeException{
    public StatusAlreadySetException(String status, String id) {
        super(String.format("Status %s already set to %s", status, id));
    }
}

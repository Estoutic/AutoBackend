package com.drujba.autobackend.exceptions.branch;

public class BranchDoesNotExistException extends RuntimeException{
    public BranchDoesNotExistException(String message) {
        super(String.format("Branch %s does not exist", message));
    }
}

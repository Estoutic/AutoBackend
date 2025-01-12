package com.drujba.autobackend.exceptions.auth;

public class RoleDoesNotExistException extends RuntimeException {
    public RoleDoesNotExistException(String name) {
        super(String.format("Role with name %s does not exist", name));

    }
}

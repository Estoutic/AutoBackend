package com.drujba.autobackend.exceptions.application;

public class ReportDoesNotExist extends RuntimeException {
    public ReportDoesNotExist(String message) {
        super("Report does not exist: " + message);
    }
}

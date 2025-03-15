package com.drujba.autobackend.exceptions.calculator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WrongParamException extends Exception {
    public WrongParamException(String message) {
        super(message);
        WrongParamException.log.info(message);
    }
}
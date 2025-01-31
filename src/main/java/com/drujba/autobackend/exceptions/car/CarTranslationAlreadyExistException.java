package com.drujba.autobackend.exceptions.car;

import com.drujba.autobackend.models.enums.Locale;

import java.util.UUID;

public class CarTranslationAlreadyExistException extends RuntimeException{
    public CarTranslationAlreadyExistException(UUID carId, Locale locale) {
        super(String.format("Car translation on %s for %s already exists",carId.toString(), locale.toString()));
    }
}

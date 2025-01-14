package com.drujba.autobackend.exceptions.car.handler;

import com.drujba.autobackend.exceptions.car.CarModelAlreadyExistException;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.auth.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CarExceptionHandler {

    @ExceptionHandler(CarModelAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleCarModelAlreadyExistException(CarModelAlreadyExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CarModelDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleCarModelAlreadyExistException(CarModelDoesNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

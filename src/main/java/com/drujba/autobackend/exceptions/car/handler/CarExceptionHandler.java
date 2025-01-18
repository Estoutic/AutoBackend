package com.drujba.autobackend.exceptions.car.handler;

import com.drujba.autobackend.exceptions.car.*;
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
    public ResponseEntity<ErrorResponse> handleCarModelDoesNotExistException(CarModelDoesNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleCarDoesNotExistException(CarDoesNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ImageDoestNotExistException.class)
    public ResponseEntity<ErrorResponse> handleImageDoestNotExistException(ImageDoestNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ImageDoesNotBelongException.class)
    public ResponseEntity<ErrorResponse> handleImageDoesNotBelongException(ImageDoesNotBelongException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

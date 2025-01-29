package com.drujba.autobackend.exceptions.application.handler;

import com.drujba.autobackend.exceptions.application.ApplicationDoesNotExistException;
import com.drujba.autobackend.exceptions.application.ReportDoesNotExist;
import com.drujba.autobackend.exceptions.application.StatusAlreadySetException;
import com.drujba.autobackend.models.dto.auth.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> applicationDoesNotExist(ApplicationDoesNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReportDoesNotExist.class)
    public ResponseEntity<ErrorResponse> applicationDoesNotExist(ReportDoesNotExist exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusAlreadySetException.class)
    public ResponseEntity<ErrorResponse> handleStatusAlreadySetException(StatusAlreadySetException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

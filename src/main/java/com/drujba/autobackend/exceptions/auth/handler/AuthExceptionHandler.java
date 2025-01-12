package com.drujba.autobackend.exceptions.auth.handler;

import com.drujba.autobackend.exceptions.auth.*;
import com.drujba.autobackend.models.dto.auth.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized",
                "wrong email or password",
                HttpStatus.UNAUTHORIZED.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handlerUserAlreadyExist(UserAlreadyExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEmailInputException.class)
    public ResponseEntity<ErrorResponse> handlerUserAlreadyExist(InvalidEmailInputException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Bad Request",
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtAuthenticationException(JwtAuthenticationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Authentication Failed",
                exception.getMessage(),
                exception.getHttpStatus()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getHttpStatus()));
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handlerUserDoesNotExist(UserAlreadyExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handlerRoleDoesNotExist(RoleDoesNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handlerRoleDoesNotExist(TokenRefreshException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

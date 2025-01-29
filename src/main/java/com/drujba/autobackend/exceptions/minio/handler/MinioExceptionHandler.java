package com.drujba.autobackend.exceptions.minio.handler;

import com.drujba.autobackend.exceptions.minio.DownloadFileException;
import com.drujba.autobackend.exceptions.minio.UpdateFileException;
import com.drujba.autobackend.exceptions.minio.UploadImageException;
import com.drujba.autobackend.models.dto.auth.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MinioExceptionHandler {

    @ExceptionHandler(UploadImageException.class)
    public ResponseEntity<ErrorResponse> handleUploadImageException(UploadImageException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DownloadFileException.class)
    public ResponseEntity<ErrorResponse> handleDownloadFileException(DownloadFileException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UpdateFileException.class)
    public ResponseEntity<ErrorResponse> handleUpdateFileException(UpdateFileException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                exception.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

//    TODO посмотреть правильный ли код ошибки возвращаем
}

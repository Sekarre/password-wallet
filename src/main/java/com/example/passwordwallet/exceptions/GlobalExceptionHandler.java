package com.example.passwordwallet.exceptions;

import com.example.passwordwallet.auth.exceptions.BadCredentialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<?> handleBadCredentialException(BadCredentialException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadKeyException.class)
    public ResponseEntity<?> handleBadKeyException(BadKeyException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}

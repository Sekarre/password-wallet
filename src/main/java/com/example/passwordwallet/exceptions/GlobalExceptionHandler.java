package com.example.passwordwallet.exceptions;

import com.example.passwordwallet.auth.exceptions.BadCredentialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialException(BadCredentialException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = buildExceptionResponse(ex, request);
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadKeyException.class)
    public ResponseEntity<ExceptionResponse> handleBadKeyException(BadKeyException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = buildExceptionResponse(ex, request);
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = buildExceptionResponse(ex, request);
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(NotFoundException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = buildExceptionResponse(ex, request);
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    private ExceptionResponse buildExceptionResponse(Exception ex, HttpServletRequest request) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }
}

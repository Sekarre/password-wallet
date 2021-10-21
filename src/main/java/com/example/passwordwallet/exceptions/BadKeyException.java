package com.example.passwordwallet.exceptions;

public class BadKeyException extends RuntimeException {

    public BadKeyException(String message) {
        super(message);
    }
}

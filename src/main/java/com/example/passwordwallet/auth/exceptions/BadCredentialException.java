package com.example.passwordwallet.auth.exceptions;

public class BadCredentialException extends RuntimeException {

    public BadCredentialException(String message) {
        super(message);
    }
}

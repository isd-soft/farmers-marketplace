package com.example.isdfarmersmarket.business.exception.custom_exceptions;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }
}

package com.example.isdfarmersmarket.business.exception.custom_exceptions;

import com.example.isdfarmersmarket.dao.enums.AuthError;

public class RefreshTokenException extends RuntimeException {
    AuthError error;
    public RefreshTokenException(AuthError error) {
        super(error.name());
        this.error = error;
    }
}

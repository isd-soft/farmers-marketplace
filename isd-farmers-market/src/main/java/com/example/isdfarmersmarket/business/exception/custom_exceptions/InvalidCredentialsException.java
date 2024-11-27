    package com.example.isdfarmersmarket.business.exception.custom_exceptions;

    import com.example.isdfarmersmarket.dao.enums.AuthError;

    public class InvalidCredentialsException extends RuntimeException {
        AuthError error;
        public InvalidCredentialsException(AuthError error) {
            super(error.name());
            this.error = error;
        }
    }

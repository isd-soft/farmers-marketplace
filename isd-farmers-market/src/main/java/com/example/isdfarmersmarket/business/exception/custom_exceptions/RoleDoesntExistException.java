package com.example.isdfarmersmarket.business.exception.custom_exceptions;

public class RoleDoesntExistException extends RuntimeException {
    public RoleDoesntExistException(String message) {
        super(message);
    }
}

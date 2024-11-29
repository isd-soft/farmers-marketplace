package com.example.isdfarmersmarket.business.exception.custom_exceptions;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }
}
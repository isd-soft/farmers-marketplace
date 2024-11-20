package com.example.isdfarmersmarket.serviceLayer.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage());

        return ResponseEntity.status(NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorDetails> handleEntityExistsException(EntityExistsException ex, WebRequest request) {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }
}

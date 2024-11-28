package com.example.isdfarmersmarket.dao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AuthError {
    // JWT Exceptions
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "The refresh token is invalid or expired."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired."),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "JWT token is unsupported."),
    MALFORMED_JWT(HttpStatus.BAD_REQUEST, "JWT token is malformed."),
    SIGNATURE_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT token signature validation failed."),
    UNEXPECTED_JWT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected JWT exception."),

    // Controller Handled Exceptions
    AUTHENTICATED_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Authenticated user not found."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists."),
    ROLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Role already exists."),
    ROLE_DOESNT_EXIST(HttpStatus.NOT_FOUND, "Role doesn't exist."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "Username not found."),
    PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "Passwords do not match."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Bad credentials."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "Authentication failed.");

    HttpStatus httpStatus;
    String message;
}

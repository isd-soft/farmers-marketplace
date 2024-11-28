package com.example.isdfarmersmarket.dao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AuthError {
    // JWT Exceptions
    REFRESH_TOKEN_INVALID("The refresh token is invalid or expired."),
    TOKEN_EXPIRED("Token has expired."),
    UNSUPPORTED_JWT("JWT token is unsupported."),
    MALFORMED_JWT("JWT token is malformed."),
    SIGNATURE_VALIDATION_FAILED("JWT token signature validation failed."),
    UNEXPECTED_JWT_ERROR("Unexpected JWT exception."),
    // Controller Handled Exceptions
    AUTHENTICATED_USER_NOT_FOUND("Authenticated user not found."),
    INVALID_CREDENTIALS("Invalid username or password."),
    EMAIL_ALREADY_EXISTS("Email already exists."),
    ROLE_ALREADY_EXISTS("Role already exists."),
    ROLE_DOESNT_EXIST("Role doesn't exist."),
    USERNAME_NOT_FOUND("Username not found."),
    PASSWORDS_DO_NOT_MATCH("Passwords do not match."),
    ACCESS_DENIED("Access denied."),
    BAD_CREDENTIALS("Bad credentials."),
    AUTHENTICATION_FAILED("Authentication failed.");

    private final String message;
}

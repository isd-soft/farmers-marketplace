package com.example.isdfarmersmarket.dao.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AuthError {
    // JWT Exceptions
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST),
    MALFORMED_JWT(HttpStatus.BAD_REQUEST),
    SIGNATURE_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED),
    UNEXPECTED_JWT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),

    // Controller Handled Exceptions
    AUTHENTICATED_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    ROLE_ALREADY_EXISTS(HttpStatus.CONFLICT),
    ROLE_DOESNT_EXIST(HttpStatus.NOT_FOUND),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND),
    PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(HttpStatus.FORBIDDEN),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_ACTIVATED(HttpStatus.FORBIDDEN),
    INVALID_APPLICATION_STATE(HttpStatus.CONFLICT);

    HttpStatus httpStatus;
}

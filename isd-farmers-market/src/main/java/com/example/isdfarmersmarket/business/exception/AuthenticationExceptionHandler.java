package com.example.isdfarmersmarket.business.exception;

import com.example.isdfarmersmarket.dao.enums.AuthError;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.*;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, AuthError.EMAIL_ALREADY_EXISTS.getMessage());
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ProblemDetail handleRoleAlreadyExists() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, AuthError.ROLE_ALREADY_EXISTS.getMessage());
    }

    @ExceptionHandler(RoleDoesntExistException.class)
    public ProblemDetail handleRoleDoesntExist() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, AuthError.ROLE_DOESNT_EXIST.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFound() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, AuthError.USERNAME_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ProblemDetail handlePasswordsDoNotMatch() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, AuthError.PASSWORDS_DO_NOT_MATCH.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, AuthError.INVALID_CREDENTIALS.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ProblemDetail handleMalformedJwtException() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, AuthError.MALFORMED_JWT.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAuthorizationDenied() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, AuthError.ACCESS_DENIED.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, AuthError.BAD_CREDENTIALS.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationFailed() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, AuthError.AUTHENTICATION_FAILED.getMessage());
    }
}

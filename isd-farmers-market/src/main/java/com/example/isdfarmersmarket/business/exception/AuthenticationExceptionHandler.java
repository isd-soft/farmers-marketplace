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

    @ExceptionHandler({EmailAlreadyExistsException.class, RoleAlreadyExistsException.class,
            RoleDoesntExistException.class, UsernameNotFoundException.class,
            PasswordsDoNotMatchException.class, InvalidCredentialsException.class,
            MalformedJwtException.class, AccessDeniedException.class,
            BadCredentialsException.class, AuthenticationException.class,
            AccountNotActivatedException.class})
    public ProblemDetail handleException(Exception ex) {
        AuthError authError = switch (ex) {
            case EmailAlreadyExistsException e -> AuthError.EMAIL_ALREADY_EXISTS;
            case RoleAlreadyExistsException e -> AuthError.ROLE_ALREADY_EXISTS;
            case RoleDoesntExistException e -> AuthError.ROLE_DOESNT_EXIST;
            case UsernameNotFoundException e -> AuthError.USERNAME_NOT_FOUND;
            case PasswordsDoNotMatchException e -> AuthError.PASSWORDS_DO_NOT_MATCH;
            case InvalidCredentialsException e -> AuthError.INVALID_CREDENTIALS;
            case MalformedJwtException e -> AuthError.MALFORMED_JWT;
            case AccessDeniedException e -> AuthError.ACCESS_DENIED;
            case BadCredentialsException e -> AuthError.BAD_CREDENTIALS;
            case AuthenticationException e -> AuthError.AUTHENTICATION_FAILED;
            case AccountNotActivatedException e -> AuthError.ACCOUNT_NOT_ACTIVATED;
            default -> AuthError.UNEXPECTED_JWT_ERROR;
        };

        return buildProblemDetail(authError);
    }

    private ProblemDetail buildProblemDetail(AuthError authError) {
        return ProblemDetail.forStatusAndDetail(authError.getHttpStatus(), authError.name());
    }
}
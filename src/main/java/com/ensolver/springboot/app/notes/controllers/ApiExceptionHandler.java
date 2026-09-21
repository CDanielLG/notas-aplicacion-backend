package com.ensolver.springboot.app.notes.controllers;

import com.ensolver.springboot.app.notes.DTO.AuthResponse;
import com.ensolver.springboot.app.notes.exception.RegistrationException;
import com.ensolver.springboot.app.notes.exception.UserDisabledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleValidationError(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Los datos enviados no son válidos");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthResponse.withMessage(message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthResponse> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.withMessage("Invalid email or password"));
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<AuthResponse> handleDisabledUser(UserDisabledException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.withMessage(exception.getMessage()));
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<AuthResponse> handleRegistrationError(RegistrationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthResponse.withMessage(exception.getMessage()));
    }
}

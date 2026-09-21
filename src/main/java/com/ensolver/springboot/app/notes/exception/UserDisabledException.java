package com.ensolver.springboot.app.notes.exception;

public class UserDisabledException extends RuntimeException {

    public UserDisabledException(String message) {
        super(message);
    }
}

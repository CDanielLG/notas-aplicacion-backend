package com.ensolver.springboot.app.notes.DTO;

public class AuthResponse {
    private String token;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public static AuthResponse withMessage(String message) {
        return new AuthResponse(null, message);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


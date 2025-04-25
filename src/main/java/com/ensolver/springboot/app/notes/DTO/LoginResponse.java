package com.ensolver.springboot.app.notes.DTO;

public class LoginResponse {
    private String token;
    
    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    
}

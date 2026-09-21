package com.ensolver.springboot.app.notes.controllers;

import com.ensolver.springboot.app.notes.DTO.AuthResponse;
import com.ensolver.springboot.app.notes.DTO.LoginRequest;
import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.security.JwtUtil;
import com.ensolver.springboot.app.notes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String email = userService.login(loginRequest.getEmail(), loginRequest.getPassword()).getEmail();
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getPasswordConfirm());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(null, "Registration successful! Please log in."));
    }
}


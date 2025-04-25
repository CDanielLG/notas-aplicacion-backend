package com.ensolver.springboot.app.notes.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.DTO.LoginRequest;
import com.ensolver.springboot.app.notes.DTO.LoginResponse;
import com.ensolver.springboot.app.notes.DTO.UserRegistrationDTO;
import com.ensolver.springboot.app.notes.entity.Usuario;
import com.ensolver.springboot.app.notes.service.SecurityService;
import com.ensolver.springboot.app.notes.service.UserService;
import com.ensolver.springboot.app.notes.validator.UserValidator;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @CrossOrigin(origins = "https://notas-aplicacion-backend.onrender.com", allowCredentials = "true")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Usuario user, 
                                        BindingResult bindingResult) {
        
        // Validación manual
        userValidator.validate(user, bindingResult);
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        userService.save(user);
        return ResponseEntity.ok(Map.of(
            "message", "Usuario registrado exitosamente",
            "email", user.getEmail()
        ));
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "https://notas-aplicacion-backend.onrender.com", allowCredentials = "true")
    public ResponseEntity<Map<String, String>> loginUser(
            @Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwtToken = securityService.generateJwtToken(authentication);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "token", jwtToken
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(
            securityService.getCurrentUserDetails()
        );
    }
}

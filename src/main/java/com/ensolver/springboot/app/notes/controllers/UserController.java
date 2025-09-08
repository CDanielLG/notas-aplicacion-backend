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
    @CrossOrigin(origins = {
        "https://misnotasweb-98015.web.app",
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "http://localhost:5501",
        "http://127.0.0.1:5501"
    
    })
    public ResponseEntity<?> registerUser(
        @Valid @RequestBody UserRegistrationDTO userDto,
        BindingResult bindingResult) {
            System.out.println("Recibido DTO: " + userDto.getEmail() + " / " + userDto.getPassword() + " / " + userDto.getPasswordConfirm());

    // Validar que password == passwordConfirm
    if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
        return ResponseEntity.badRequest().body(Map.of("error", "Las contraseñas no coinciden"));
    }

    // Checar si ya existe
    if (userService.findByEmail(userDto.getEmail()) != null) {
        return ResponseEntity.badRequest().body(Map.of("error", "El correo ya está registrado"));
    }

    // Crear la entidad Usuario
    Usuario user = new Usuario();
    user.setEmail(userDto.getEmail());
    user.setPassword(userDto.getPassword()); // se encripta en el service

    userService.save(user);

    return ResponseEntity.ok(Map.of(
        "message", "Usuario registrado exitosamente",
        "email", user.getEmail()
    ));
}

    @PostMapping("/login")
    @CrossOrigin(origins = "https://misnotasweb-98015.web.app", allowCredentials = "true")
    public ResponseEntity<Map<String, String>> loginUser(
            @Valid @RequestBody LoginRequest loginRequest) {
                Usuario user = userService.findByEmail(loginRequest.getEmail());
                if (user == null) {
                    return ResponseEntity.status(401).body(Map.of("error", "El correo ingresado no está registrado"));
                }
            
                try {
                    // Paso 2: intentar autenticación
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
            
                } catch (Exception e) {
                    // Si la contraseña está mal
                    return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
                }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(
            securityService.getCurrentUserDetails()
        );
    }
}

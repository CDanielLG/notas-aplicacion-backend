package com.ensolver.springboot.app.notes.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {
        String generateJwtToken(Authentication authentication);
    UserDetails getCurrentUserDetails();

}

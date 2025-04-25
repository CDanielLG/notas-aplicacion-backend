package com.ensolver.springboot.app.notes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ensolver.springboot.app.notes.security.JwtTokenProvider;

@Service
public class SecurityServiceImpl implements SecurityService{
 
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public String generateJwtToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public UserDetails getCurrentUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    }
}

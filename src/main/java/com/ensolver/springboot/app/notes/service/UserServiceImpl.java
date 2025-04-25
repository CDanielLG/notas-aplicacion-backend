package com.ensolver.springboot.app.notes.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ensolver.springboot.app.notes.entity.Usuario;
import com.ensolver.springboot.app.notes.repositories.RoleRepository;
import com.ensolver.springboot.app.notes.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void save(Usuario user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Usuario findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

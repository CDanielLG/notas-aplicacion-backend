package com.ensolver.springboot.app.notes.service;

import com.ensolver.springboot.app.notes.DTO.UserDTO;
import com.ensolver.springboot.app.notes.entity.Role;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.exception.RegistrationException;
import com.ensolver.springboot.app.notes.exception.UserDisabledException;
import com.ensolver.springboot.app.notes.repo.IRoleRepo;
import com.ensolver.springboot.app.notes.repo.IUserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepo userRepo;
    private final IRoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepo userRepo, IRoleRepo roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public Optional<User> findByEmailOptional(String email) {
        return userRepo.findByEmail(email);
    }

    public User save(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(userDTO.isEnabled());
        user.setRole(userDTO.getRole());
        return userRepo.save(user);
    }

    public User register(String email, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new RegistrationException("Passwords do not match");
        }
        if (findByEmailOptional(email).isPresent()) {
            throw new RegistrationException("Email already registered");
        }
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException(
                        "Default role USER does not exist"));
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRole(defaultRole);
        return userRepo.save(user);
    }

    public User update(Integer id, UserDTO userDTO) {
        User currentUser = findById(id);
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        currentUser.setEnabled(userDTO.isEnabled());
        if (userDTO.getRole() != null) {
            currentUser.setRole(userDTO.getRole());
        }
        return userRepo.save(currentUser);
    }

    public void deleteById(Integer id) {
        if (!userRepo.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }

    public User login(String email, String password) {
        User user = findByEmailOptional(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        if (!user.isEnabled()) {
            throw new UserDisabledException("User is disabled");
        }
        return user;
    }
}

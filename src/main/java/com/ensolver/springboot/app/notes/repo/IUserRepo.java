package com.ensolver.springboot.app.notes.repo;

import com.ensolver.springboot.app.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}


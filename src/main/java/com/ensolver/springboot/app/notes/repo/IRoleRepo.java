package com.ensolver.springboot.app.notes.repo;

import com.ensolver.springboot.app.notes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepo extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}

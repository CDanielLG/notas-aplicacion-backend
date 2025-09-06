package com.ensolver.springboot.app.notes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ensolver.springboot.app.notes.entity.Usuario;

public interface UserRepository   extends JpaRepository<Usuario, Long>{
   Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}

package com.ensolver.springboot.app.notes.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ensolver.springboot.app.notes.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

}

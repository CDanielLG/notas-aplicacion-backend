package com.ensolver.springboot.app.notes.service;

import com.ensolver.springboot.app.notes.entity.Usuario;

public interface UserService {

    void save(Usuario user);

    Usuario findByEmail(String email);
}

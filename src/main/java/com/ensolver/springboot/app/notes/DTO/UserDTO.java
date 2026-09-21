package com.ensolver.springboot.app.notes.DTO;

import com.ensolver.springboot.app.notes.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserDTO {
    private Integer user_id;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private boolean enabled = true;

    @NotNull
    private Role role;

    public UserDTO() {
    }

    public UserDTO(Integer user_id, String email, String password, boolean enabled, Role role) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

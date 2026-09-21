package com.ensolver.springboot.app.notes.DTO;

import jakarta.validation.constraints.NotNull;

public class RoleDto {

    private Integer id_role;

    @NotNull
    private String name;
    @NotNull
    private boolean enabled;
}

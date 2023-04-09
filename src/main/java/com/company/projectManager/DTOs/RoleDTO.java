package com.company.projectManager.DTOs;

import com.company.projectManager.utils.RoleName;
import jakarta.validation.constraints.NotNull;


public record RoleDTO (
        Long id,

        @NotNull
        RoleName name
) {
}

package com.company.projectManager.common.dto;

import com.company.projectManager.common.utils.RoleName;
import jakarta.validation.constraints.NotNull;


public record RoleDTO (
        Long id,

        @NotNull
        RoleName name
) {
}

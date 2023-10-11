package com.company.projectManager.common.dto;

import com.company.projectManager.common.entity.Authority;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record RoleDTO (
        Long id,

        @NotNull
        String name,

        List<Authority> authorities,

        @NotNull
        BusinessUnitDTO businessUnit
) {
}

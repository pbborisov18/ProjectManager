package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

public record UserBusinessUnitRoleDTO(

        Long id,

        @NotNull
        UserWithoutPasswordDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        RoleDTO role
) {

}

package com.company.projectManager.DTOs;

import jakarta.validation.constraints.NotNull;

public record UserBusinessUnitRoleDTO(

        @NotNull
        UserWithoutPasswordDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        RoleDTO role
) {

}

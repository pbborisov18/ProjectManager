package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

public record UserWithPassBusinessUnitRoleDTO(

        @NotNull
        UserDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        RoleDTO role
) {

}

package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

public record UserNoPassBusinessUnitRoleDTO(

        Long id,

        @NotNull
        UserNoPassDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        RoleDTO role
) {

}

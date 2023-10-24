package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserBusinessUnitDTO(

        Long id,

        @NotNull
        UserDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        List<RoleDTO> roles
) {

}

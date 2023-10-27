package com.company.projectManager.common.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserDTO;
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

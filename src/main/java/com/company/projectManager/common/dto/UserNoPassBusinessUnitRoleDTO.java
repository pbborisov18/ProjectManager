package com.company.projectManager.common.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserNoPassBusinessUnitRoleDTO(

        Long id,

        @NotNull
        UserNoPassDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        List<RoleDTO> roles
) {

}
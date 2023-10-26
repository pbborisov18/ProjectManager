package com.company.projectManager.common.dto;

import com.company.projectManager.common.dto.user.UserNoPassDTO;
import jakarta.validation.constraints.NotNull;

public record UserNoPassBusinessUnitDTO(

        Long id,

        @NotNull
        UserNoPassDTO user,

        @NotNull
        BusinessUnitDTO businessUnit
) {

}

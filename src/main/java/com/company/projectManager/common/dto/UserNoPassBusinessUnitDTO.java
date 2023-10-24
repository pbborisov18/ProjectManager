package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

public record UserNoPassBusinessUnitDTO(

        Long id,

        @NotNull
        UserNoPassDTO user,

        @NotNull
        BusinessUnitDTO businessUnit
) {

}

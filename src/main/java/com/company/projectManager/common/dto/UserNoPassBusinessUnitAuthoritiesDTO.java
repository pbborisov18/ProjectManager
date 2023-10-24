package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserNoPassBusinessUnitAuthoritiesDTO(

        Long id,

        @NotNull
        UserNoPassDTO user,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        List<AuthorityDTO> authorityDTOList
){}

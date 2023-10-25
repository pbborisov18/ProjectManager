package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BusinessUnitAuthoritiesDTO(

        Long id,

        @NotNull
        BusinessUnitDTO businessUnit,

        @NotNull
        List<AuthorityDTO> authorityDTOList
){}

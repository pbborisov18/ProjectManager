package com.company.projectManager.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorityDTO(

        Long id,

        @NotNull
        @NotBlank
        String name
) {
}

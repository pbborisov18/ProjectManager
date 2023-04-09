package com.company.projectManager.DTOs;

import com.company.projectManager.utils.TypeName;
import jakarta.validation.constraints.NotNull;

public record TypeDTO(
        Long id,

        @NotNull
        TypeName name
) {

}

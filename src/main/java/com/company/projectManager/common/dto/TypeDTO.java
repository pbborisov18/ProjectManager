package com.company.projectManager.common.dto;

import com.company.projectManager.common.utils.TypeName;
import jakarta.validation.constraints.NotNull;

public record TypeDTO(
        Long id,

        @NotNull
        TypeName name
) {

}

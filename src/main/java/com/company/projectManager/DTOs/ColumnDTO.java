package com.company.projectManager.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ColumnDTO(
        Long id,

        @NotNull @NotBlank
        String name,

        @NotNull
        WhiteboardDTO whiteboardDTO,

        @NotNull
        int position
) {

}

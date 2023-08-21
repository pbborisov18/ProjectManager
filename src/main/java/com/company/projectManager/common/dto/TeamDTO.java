package com.company.projectManager.common.dto;

import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record TeamDTO(
        Long id,

        @NotNull
        @NotBlank
        String name,

        @NotNull
        TypeDTO type,

        @NotNull
        CompanyDTO company,

        @NotNull
        ProjectDTO project,

        @Nullable
        WhiteboardDTO whiteboard

) implements BusinessUnitDTO{
}

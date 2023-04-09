package com.company.projectManager.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record ProjectDTO(
        Long id,

        @NotNull
        @NotBlank
        String name,

        @NotNull
        TypeDTO type,

        @NotNull
        CompanyDTO company,

        @Nullable
        WhiteboardDTO whiteboard

) implements BusinessUnitDTO{

        public ProjectDTO project(){
                return null;
        }
}

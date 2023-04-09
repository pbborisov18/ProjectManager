package com.company.projectManager.DTOs;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record CompanyDTO(
        Long id,

        @NotNull
        @NotBlank
        String name,

        @NotNull
        TypeDTO type,

        @Nullable
        WhiteboardDTO whiteboard

) implements BusinessUnitDTO{

        public CompanyDTO company(){
                return null;
        }

        public ProjectDTO project(){
                return null;
        }

}

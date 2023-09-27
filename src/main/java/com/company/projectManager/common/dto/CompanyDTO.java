package com.company.projectManager.common.dto;

import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record CompanyDTO(
        Long id,

        @NotNull
        @NotBlank
        String name,

        @NotNull
        TypeName type,

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

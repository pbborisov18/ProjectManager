package com.company.projectManager.DTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record WhiteboardDTO(
        Long id,

        @NotNull
        @NotBlank
        String name
) {
}

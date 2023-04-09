package com.company.projectManager.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record NoteDTO(
        Long id,

        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        String description,

        @NotNull
        WhiteboardDTO whiteboard
) {
}

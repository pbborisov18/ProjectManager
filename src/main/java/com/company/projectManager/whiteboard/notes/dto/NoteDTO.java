package com.company.projectManager.whiteboard.notes.dto;

import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
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
        ColumnDTO columnDTO
) {
}

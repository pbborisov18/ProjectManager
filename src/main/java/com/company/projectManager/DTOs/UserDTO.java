package com.company.projectManager.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record UserDTO(
        Long id,

        @Email
        @NotNull
        @NotBlank
        String email,

        @NotNull
        @NotBlank
        String password
) {
}

package com.company.projectManager.DTOs;

import com.company.projectManager.utils.InviteState;
import jakarta.validation.constraints.NotNull;

public record InviteDTO(
        Long id,

        @NotNull
        InviteState state,

        @NotNull
        UserDTO sender,

        @NotNull
        UserDTO receiver,

        @NotNull
        BusinessUnitDTO businessUnit
) {
}

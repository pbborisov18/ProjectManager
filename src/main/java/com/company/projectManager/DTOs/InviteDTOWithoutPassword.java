package com.company.projectManager.DTOs;

import com.company.projectManager.utils.InviteState;
import jakarta.validation.constraints.NotNull;

public record InviteDTOWithoutPassword(
        Long id,

        @NotNull
        InviteState state,

        @NotNull
        UserWithoutPasswordDTO sender,

        @NotNull
        UserWithoutPasswordDTO receiver,

        @NotNull
        BusinessUnitDTO businessUnit
){
}

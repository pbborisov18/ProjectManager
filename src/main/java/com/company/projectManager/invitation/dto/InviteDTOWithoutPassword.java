package com.company.projectManager.invitation.dto;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserWithoutPasswordDTO;
import com.company.projectManager.common.utils.InviteState;
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

package com.company.projectManager.invitation.dto;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserDTO;
import com.company.projectManager.common.utils.InviteState;
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

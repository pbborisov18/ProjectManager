package com.company.projectManager.invitation.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserDTO;
import com.company.projectManager.common.utils.InviteState;
import jakarta.validation.constraints.NotNull;

public record InviteDTO(
        Long id,

        @NotNull
        InviteState state,

        @NotNull
        UserDTO receiver,

        @NotNull
        BusinessUnitDTO businessUnit
) {
}

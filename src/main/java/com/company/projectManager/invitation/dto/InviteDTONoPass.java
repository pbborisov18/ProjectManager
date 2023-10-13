package com.company.projectManager.invitation.dto;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserNoPassDTO;
import com.company.projectManager.common.utils.InviteState;
import jakarta.validation.constraints.NotNull;

public record InviteDTONoPass(
        Long id,

        @NotNull
        InviteState state,

        @NotNull
        UserNoPassDTO sender,

        @NotNull
        UserNoPassDTO receiver,

        @NotNull
        BusinessUnitDTO businessUnit
){
}

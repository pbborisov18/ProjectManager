package com.company.projectManager.invitation.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;

public record BusinessUnitUserNoPassDTO(
        BusinessUnitDTO businessUnitDTO,

        UserNoPassDTO userNoPassDTO
) {
}

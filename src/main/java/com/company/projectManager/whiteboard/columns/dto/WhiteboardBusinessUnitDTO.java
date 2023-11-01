package com.company.projectManager.whiteboard.columns.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

public record WhiteboardBusinessUnitDTO(

        WhiteboardDTO whiteboardDTO,

        BusinessUnitDTO businessUnitDTO
) {
}

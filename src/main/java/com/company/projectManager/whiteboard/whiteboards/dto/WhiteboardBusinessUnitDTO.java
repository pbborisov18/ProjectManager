package com.company.projectManager.whiteboard.whiteboards.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;

public record WhiteboardBusinessUnitDTO(

        WhiteboardDTO whiteboardDTO,

        BusinessUnitDTO businessUnitDTO

) {
}

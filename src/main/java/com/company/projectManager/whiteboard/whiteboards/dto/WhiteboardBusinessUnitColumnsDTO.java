package com.company.projectManager.whiteboard.whiteboards.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;

import java.util.List;

public record WhiteboardBusinessUnitColumnsDTO(
        WhiteboardDTO whiteboardDTO,

        BusinessUnitDTO businessUnitDTO,

        List<ColumnDTO> columns

) {
}
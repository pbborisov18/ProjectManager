package com.company.projectManager.whiteboard.notes.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;

public record ColumnBusinessUnitDTO (
        ColumnDTO columnDTO,

        BusinessUnitDTO  businessUnitDTO
){
}

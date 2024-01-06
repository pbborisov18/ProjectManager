package com.company.projectManager.whiteboard.columns.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;

public record ColumnBusinessUnitDTO(
        ColumnDTO columnDTO,

        BusinessUnitDTO businessUnitDTO

) {
}

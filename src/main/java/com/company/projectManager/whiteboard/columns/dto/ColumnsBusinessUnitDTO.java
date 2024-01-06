package com.company.projectManager.whiteboard.columns.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;

import java.util.List;

public record ColumnsBusinessUnitDTO(
        List<ColumnDTO> columns,

        BusinessUnitDTO businessUnitDTO
) {
}

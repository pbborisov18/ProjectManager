package com.company.projectManager.whiteboard.notes.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;

public record NoteBusinessUnitDTO(
        NoteDTO noteDTO,

        BusinessUnitDTO businessUnitDTO
) {
}

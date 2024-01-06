package com.company.projectManager.whiteboard.notes.dto;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;

import java.util.List;

public record NotesBusinessUnitDTO(
        List<NoteDTO> notes,

        BusinessUnitDTO businessUnitDTO
) {
}

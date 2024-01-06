package com.company.projectManager.whiteboard.whiteboards.service;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

import java.util.List;


public interface WhiteboardService {

    WhiteboardDTO findWhiteboard(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, EntityNotFoundException;

    void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, EntityNotFoundException;

    void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO, List<ColumnDTO> columns) throws FailedToSaveException, EntityNotFoundException;

    void deleteWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException, EntityNotFoundException;

}

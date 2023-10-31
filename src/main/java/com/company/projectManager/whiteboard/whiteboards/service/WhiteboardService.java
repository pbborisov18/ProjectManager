package com.company.projectManager.whiteboard.whiteboards.service;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

import java.util.List;


public interface WhiteboardService {

    WhiteboardDTO findWhiteboard(BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, FailedToSelectException, EntityNotFoundException;

    void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotAuthorizedException, EntityNotFoundException;

    void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO, List<ColumnDTO> columns) throws FailedToSaveException, UserUnauthenticatedException, UserNotAuthorizedException, EntityNotFoundException;

    void deleteWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotAuthorizedException, FailedToSelectException, FailedToDeleteException, EntityNotFoundException;

}

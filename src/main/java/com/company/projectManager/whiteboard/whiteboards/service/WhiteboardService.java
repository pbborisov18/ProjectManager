package com.company.projectManager.whiteboard.whiteboards.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

import java.util.List;


public interface WhiteboardService {

    void saveWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToSaveException;

    void updateWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToDeleteException, EntityNotFoundException;

    WhiteboardDTO findWhiteboardById(Long id) throws FailedToSelectException, EntityNotFoundException;

    WhiteboardDTO findWhiteboardByIdByAuthenticatedUser(BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSelectException, EntityNotFoundException;

    void createWhiteboardWithAuthenticatedUser(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException;

    void deleteWhiteboardWithAuthenticatedUser(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSelectException, FailedToDeleteException, EntityNotFoundException;

    List<WhiteboardDTO> findAllWhiteboards() throws FailedToSelectException, EntityNotFoundException;

}

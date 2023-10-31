package com.company.projectManager.whiteboard.columns.service;


import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

import java.util.List;


public interface ColumnService {

    List<ColumnDTO> findAllColumnsByWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToSelectException, UserUnauthenticatedException, EntityNotFoundException;

    void createColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, FailedToSaveException, UserNotAuthorizedException;

    void updateColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToUpdateException;

    void updateColumns(List<ColumnDTO> columns)  throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToUpdateException;

    void deleteColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotAuthorizedException;

}
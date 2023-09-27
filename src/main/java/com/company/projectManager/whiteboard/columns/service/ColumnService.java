package com.company.projectManager.whiteboard.columns.service;


import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;

import java.util.List;


public interface ColumnService {

    void saveColumn(ColumnDTO columnDTO) throws FailedToSaveException;

    void updateColumn(ColumnDTO columnDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteColumn(ColumnDTO columnDTO) throws FailedToDeleteException, EntityNotFoundException;

    void deleteColumn(List<ColumnDTO> columnDTOs) throws FailedToDeleteException, EntityNotFoundException;

    ColumnDTO findColumnById(Long id) throws FailedToSelectException, EntityNotFoundException;

    List<ColumnDTO> findAllColumns() throws FailedToSelectException, EntityNotFoundException;

    List<ColumnDTO> findAllColumnsByWhiteboardId(Long id) throws FailedToSelectException, EntityNotFoundException;

    List<ColumnDTO> findAllColumnsByWhiteboardIdByAuthenticatedUser(Long id) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, EntityNotFoundException;

    void createColumnByAuthenticatedUser(ColumnDTO columnDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSaveException, UserNotAuthorizedException;

    void deleteColumnByAuthenticatedUser(ColumnDTO columnDTO, BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, UserNotAuthorizedException;

    void initializeDefaultColumns(Whiteboard whiteboard) throws FailedToSaveException;

}

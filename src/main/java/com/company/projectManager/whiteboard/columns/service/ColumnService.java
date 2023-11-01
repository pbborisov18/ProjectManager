package com.company.projectManager.whiteboard.columns.service;


import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

import java.util.List;


public interface ColumnService {

    List<ColumnDTO> findAllColumnsByWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToSelectException, EntityNotFoundException;

    void createColumn(ColumnDTO columnDTO) throws FailedToSaveException;

    void updateColumn(ColumnDTO columnDTO) throws FailedToSaveException;

    void updateColumns(List<ColumnDTO> columns) throws  FailedToUpdateException;

    void deleteColumn(ColumnDTO columnDTO) throws FailedToDeleteException;

}
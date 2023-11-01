package com.company.projectManager.whiteboard.columns.service.impl;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import com.company.projectManager.whiteboard.columns.mapper.ColumnMapper;
import com.company.projectManager.whiteboard.columns.repository.ColumnRepository;
import com.company.projectManager.whiteboard.columns.service.ColumnService;
import com.company.projectManager.whiteboard.notes.repository.NoteRepository;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnMapper columnMapper;

    private final ColumnRepository columnRepository;


    private final NoteRepository noteRepository;

    public ColumnServiceImpl(ColumnMapper columnMapper, ColumnRepository columnRepository, NoteRepository noteRepository) {
        this.columnMapper = columnMapper;
        this.columnRepository = columnRepository;
        this.noteRepository = noteRepository;
    }

    public List<ColumnDTO> findAllColumnsByWhiteboard(WhiteboardDTO whiteboard) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Column> columns = columnRepository.findAllByWhiteboardId(whiteboard.id());

            if (columns.isEmpty()) {
                throw new EntityNotFoundException("Columns not found!");
            }

            return columnMapper.toDTO(columns);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select!" + e.getMessage());
        }
    }

    public void createColumn(ColumnDTO columnDTO) throws FailedToSaveException {
        try {
            columnRepository.save(columnMapper.toEntity(columnDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }

    //Reason I'm leaving it like this is cuz it will be pain in the ass to manage in the frontend.
    //Knowing which column has to be called on create and which on update
    //So I'll just leave it like this and let luck handle it
    //(no id -> new column, id -> update)
    public void updateColumn(ColumnDTO columnDTO) throws FailedToSaveException {
        createColumn(columnDTO);
    }

    //Reason I'm leaving it like this is cuz it will be pain in the ass to manage in the frontend.
    //Knowing which column has to be called on create and which on update
    //So I'll just leave it like this and let luck handle it
    //(no id -> new column, id -> update)
    public void updateColumns(List<ColumnDTO> columns) throws FailedToUpdateException {
        try {
            columnRepository.saveAll(columnMapper.toEntity(columns));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteColumn(ColumnDTO columnDTO) throws FailedToDeleteException {
        try {

            Column column = columnMapper.toEntity(columnDTO);
            //Can't cascade from the child. Making the relationship bidirectional will open way more work at the end. So just manual deletion :(
            noteRepository.deleteNotesByColumnId(column.getId());

            columnRepository.delete(column);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed ot delete! " + e.getMessage());
        }
    }
}

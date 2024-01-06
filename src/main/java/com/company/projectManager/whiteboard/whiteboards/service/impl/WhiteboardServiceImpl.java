package com.company.projectManager.whiteboard.whiteboards.service.impl;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.repository.BusinessUnitRepository;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import com.company.projectManager.whiteboard.columns.mapper.ColumnMapper;
import com.company.projectManager.whiteboard.columns.repository.ColumnRepository;
import com.company.projectManager.whiteboard.notes.repository.NoteRepository;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import com.company.projectManager.whiteboard.whiteboards.mapper.WhiteboardMapper;
import com.company.projectManager.whiteboard.whiteboards.repository.WhiteboardRepository;
import com.company.projectManager.whiteboard.whiteboards.service.WhiteboardService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WhiteboardServiceImpl implements WhiteboardService {

    private final WhiteboardRepository whiteboardRepository;

    private final WhiteboardMapper whiteboardMapper;


    private final ColumnRepository columnRepository;

    private final ColumnMapper columnMapper;


    private final NoteRepository noteRepository;


    private final BusinessUnitRepository businessUnitRepository;

    public WhiteboardServiceImpl(WhiteboardRepository whiteboardRepository, WhiteboardMapper whiteboardMapper, BusinessUnitRepository businessUnitRepository, NoteRepository noteRepository, ColumnRepository columnRepository, ColumnMapper columnMapper) {
        this.whiteboardRepository = whiteboardRepository;
        this.whiteboardMapper = whiteboardMapper;
        this.businessUnitRepository = businessUnitRepository;
        this.noteRepository = noteRepository;
        this.columnRepository = columnRepository;
        this.columnMapper = columnMapper;
    }

    public WhiteboardDTO findWhiteboard(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, EntityNotFoundException  {
        try {
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }

            if(businessUnit.get().getWhiteboard() == null){
                throw new EntityNotFoundException("Whiteboard doesn't exist");
            }

            return whiteboardMapper.toDTO(businessUnit.get().getWhiteboard());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    @Transactional
    public void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }

            Whiteboard whiteboard = whiteboardMapper.toEntity(whiteboardDTO);

            whiteboardRepository.save(whiteboard);

            //Initialize the default columns
            initializeDefaultColumns(whiteboard);

            //Assign the whiteboard to the businessUnit
            businessUnit.get().setWhiteboard(whiteboard);

            //Save the business unit and whiteboard cuz cascade
            businessUnitRepository.save(businessUnit.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to save!" + e.getMessage());
        }
    }

    @Transactional
    public void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO, List<ColumnDTO> columns) throws FailedToSaveException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }

            Whiteboard whiteboard = whiteboardMapper.toEntity(whiteboardDTO);
            whiteboardRepository.save(whiteboard);

            //Since I can't have "backwards" cascading I have to save the columns manually
            List<Column> columnEntities = columnMapper.toEntity(columns);
            columnRepository.saveAll(columnEntities);

            //Assign the whiteboard to the businessUnit
            businessUnit.get().setWhiteboard(whiteboard);
            businessUnitRepository.save(businessUnit.get());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to save!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }

            //Since the method is transactional the order we do these in shouldn't matter.
            //Since whiteboard doesn't have any relationships (which it knows about) everything has to be handled manually
            //Basically all relationships are backwards for easier time with the mapper

            //Update that Business Unit won't have a relationship with the whiteboard
            businessUnit.get().setWhiteboard(null);
            businessUnitRepository.save(businessUnit.get());

            //Delete Notes
            for (Column column : columnRepository.findAllByWhiteboardId(whiteboardDTO.id())) {
                noteRepository.deleteNotesByColumnId(column.getId());
            }

            //Delete Columns
            columnRepository.deleteAllByWhiteboardId(whiteboardDTO.id());

            //Delete Whiteboard
            whiteboardRepository.delete(whiteboardMapper.toEntity(whiteboardDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete!" + e.getMessage());
        }
    }

    //Why is this public? Cuz transactional doesn't work on private methods
    @Transactional(propagation = Propagation.MANDATORY)
    public void initializeDefaultColumns(Whiteboard whiteboard) throws FailedToSaveException {
        try {
            Column column1 = new Column(null, "To do", whiteboard, 1L);
            Column column2 = new Column(null, "Doing", whiteboard, 2L);
            Column column3 = new Column(null, "Testing", whiteboard, 3L);
            Column column4 = new Column(null, "Done", whiteboard, 4L);

            //Since hibernate isn't stupid it will only execute this after the whiteboard is already in the db
            columnRepository.saveAll(List.of(column1, column2, column3, column4));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }
}

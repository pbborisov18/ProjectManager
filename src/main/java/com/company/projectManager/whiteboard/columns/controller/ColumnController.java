package com.company.projectManager.whiteboard.columns.controller;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnBusinessUnitDTO;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.dto.ColumnsBusinessUnitDTO;
import com.company.projectManager.whiteboard.columns.dto.WhiteboardBusinessUnitDTO;
import com.company.projectManager.whiteboard.columns.service.ColumnService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ColumnController {

    private final ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    //Why is this Post? Cuz something something GET requests with bodies is bad (and spring doesn't read it),
    //something something, expose the data in the url (no)
    //I'm either breaking one standard or the other. This one is worse to break but easier to implement.
    //(You might say I'm a total idiot for doing this and I'd agree with you)
    @PostMapping(value = {"/company/columns", "/company/project/columns", "/company/project/team/columns"})
    @PreAuthorize("authorityCheck(#whiteboardBUDTO.businessUnitDTO().id(), \"ManageWhiteboard\")")
    public ResponseEntity<Object> getAllColumnsOfWhiteboard(@RequestBody @Valid WhiteboardBusinessUnitDTO whiteboardBUDTO){
        try {
            List<ColumnDTO> columns = columnService.findAllColumnsByWhiteboard(whiteboardBUDTO.whiteboardDTO());

            return new ResponseEntity<>(columns, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/createColumn", "/company/project/createColumn", "/company/project/team/createColumn"})
    @PreAuthorize("authorityCheck(#columnBUDTO.businessUnitDTO().id(), \"ManageWhiteboard\")")
    public ResponseEntity<Object> createColumn(@RequestBody @Valid ColumnBusinessUnitDTO columnBUDTO) {
        try {
            columnService.createColumn(columnBUDTO.columnDTO());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/company/updateColumns", "/company/project/updateColumns", "/company/project/team/updateColumns"})
    @PreAuthorize("authorityCheck(#columnsBUDTO.businessUnitDTO().id(), \"ManageWhiteboard\")")
    public ResponseEntity<Object> updateColumns(@RequestBody @Valid ColumnsBusinessUnitDTO columnsBUDTO) {
        try {
            columnService.updateColumns(columnsBUDTO.columns());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/company/updateColumn", "/company/project/updateColumn", "/company/project/team/updateColumn"})
    @PreAuthorize("authorityCheck(#columnBUDTO.businessUnitDTO().id(), \"ManageWhiteboard\")")
    public ResponseEntity<Object> updateColumn(@RequestBody @Valid ColumnBusinessUnitDTO columnBUDTO) {
        try {
            columnService.updateColumn(columnBUDTO.columnDTO());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/company/deleteColumn", "/company/project/deleteColumn", "/company/project/team/deleteColumn"})
    @PreAuthorize("authorityCheck(#columnBUDTO.businessUnitDTO().id(), \"ManageWhiteboard\")")
    public ResponseEntity<Object> deleteColumn(@RequestBody @Valid ColumnBusinessUnitDTO columnBUDTO) {
        try {
            columnService.deleteColumn(columnBUDTO.columnDTO());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException  e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

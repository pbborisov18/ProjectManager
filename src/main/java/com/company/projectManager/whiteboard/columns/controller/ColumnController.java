package com.company.projectManager.whiteboard.columns.controller;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.columns.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ColumnController {

    @Autowired
    ColumnService columnService;

    @PostMapping(value = {"/company/columns", "/company/project/columns", "/company/project/team/columns"})
    public ResponseEntity<Object> getAllColumnsOfWhiteboard(@RequestBody WhiteboardDTO whiteboardDTO){
        try {
            List<ColumnDTO> columns = columnService.findAllColumnsByWhiteboardIdByAuthenticatedUser(whiteboardDTO.id());

            return new ResponseEntity<>(columns, HttpStatus.OK);
        }  catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/createColumn", "/company/project/createColumn", "/company/project/team/createColumn"})
    public ResponseEntity<Object> createColumn(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - ColumnDTO columnDTO, BusinessUnitDTO businessUnitDTO*/ ) {
        try {
            columnService.createColumnByAuthenticatedUser((ColumnDTO) requestBody.get("columnDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSelectException | FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        }  catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = {"/company/deleteColumn", "/company/project/deleteColumn", "/company/project/team/deleteColumn"})
    public ResponseEntity<Object> deleteColumn(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - ColumnDTO columDTO, BusinessUnitDTO businessUnitDTO*/ ) {
        try {
            columnService.deleteColumnByAuthenticatedUser( (ColumnDTO) requestBody.get("columnDTO"), (BusinessUnitDTO) requestBody.get("BusinessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

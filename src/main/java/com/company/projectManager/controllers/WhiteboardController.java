package com.company.projectManager.controllers;

import com.company.projectManager.DTOs.BusinessUnitDTO;
import com.company.projectManager.DTOs.WhiteboardDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.services.NoteService;
import com.company.projectManager.services.WhiteboardService;
import com.company.projectManager.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class WhiteboardController {

    @Autowired
    WhiteboardService whiteboardService;

    @Autowired
    NoteService noteService;

    @PostMapping(value = {"/company/whiteboard","/company/project/whiteboard", "/company/project/team/whiteboard"})
    public ResponseEntity<Object> getWhiteboardOfCompany(@RequestBody BusinessUnitDTO businessUnitDTO) {
        try {

            WhiteboardDTO whiteboardDTO = whiteboardService.findWhiteboardByIdByAuthenticatedUser(businessUnitDTO);

            //if whiteboard is found return it
            return new ResponseEntity<>(whiteboardDTO, HttpStatus.OK);
        } catch (UserUnauthenticatedException | UserNotInBusinessUnitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e){
            //if it isn't send a request back asking to redirect to */createWhiteboard
            return new ResponseEntity<>("Redirect", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/createWhiteboard", "/company/project/createWhiteboard", "/company/project/team/createWhiteboard"})
    public ResponseEntity<Object> createWhiteboard(@RequestBody Map<String, Object> requestBody/*Waiting for these 2 objects - WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO*/){
        try {
            whiteboardService.createWhiteboardWithAuthenticatedUser((WhiteboardDTO) requestBody.get("whiteboardDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = {"/company/deleteWhiteboard", "/company/project/deleteWhiteboard", "/company/project/team/deleteWhiteboard"})
    public ResponseEntity<Object> deleteWhiteboard(@RequestBody Map<String, Object> requestBody/*Waiting for these 2 objects - WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO*/){
        try {
            whiteboardService.deleteWhiteboardWithAuthenticatedUser((WhiteboardDTO) requestBody.get("whiteboardDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSaveException | FailedToSelectException | FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (com.company.projectManager.exceptions.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

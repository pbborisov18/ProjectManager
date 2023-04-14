package com.company.projectManager.controllers;

import com.company.projectManager.DTOs.BusinessUnitDTO;
import com.company.projectManager.DTOs.ColumnDTO;
import com.company.projectManager.DTOs.NoteDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.services.NoteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NoteController {

    @Autowired
    NoteService noteService;

    @PostMapping(value = {"/company/whiteboard/notes", "/company/project/whiteboard/notes", "/company/project/team/whiteboard/notes"})
    public ResponseEntity<Object> getAllNotesOfColumn(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - ColumnDTO columnDTO, BusinessUnitDTO businessUnitDTO*/){
        try {
            List<NoteDTO> notes = noteService.findAllNotesByColumnIdByAuthenticatedUser((ColumnDTO) requestBody.get("columnDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (com.company.projectManager.exceptions.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/whiteboard/createNote", "/company/project/whiteboard/createNote", "/company/project/team/whiteboard/createNote"})
    public ResponseEntity<Object> createNote(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - NoteDTO noteDTO, BusinessUnitDTO*/) {
        try {
            noteService.createNoteByAuthenticatedUser((NoteDTO) requestBody.get("noteDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSelectException | FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/company/whiteboard/updateNote", "/company/project/whiteboard/updateNote", "/company/project/team/whiteboard/updateNote"})
    public ResponseEntity<Object> updateNote(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - NoteDTO noteDTO, BusinessUnitDTO*/){
        try {
            noteService.updateNoteByAuthenticatedUser((NoteDTO) requestBody.get("noteDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSelectException | FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (com.company.projectManager.exceptions.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping(value = {"/company/whiteboard/deleteNote", "/company/project/whiteboard/deleteNote", "/company/project/team/whiteboard/deleteNote"})
    public ResponseEntity<Object> deleteNote(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - NoteDTO noteDTO, BusinessUnitDTO*/){
        try {
            noteService.deleteNoteByAuthenticatedUser((NoteDTO) requestBody.get("noteDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSelectException | FailedToDeleteException | EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (com.company.projectManager.exceptions.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
}
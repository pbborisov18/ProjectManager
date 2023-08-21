package com.company.projectManager.whiteboard.notes.controller;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.service.NoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
    public ResponseEntity<Object> getAllNotesOfColumn(@RequestBody ColumnDTO columnDTO){
        try {
            List<NoteDTO> notes = noteService.findAllNotesByColumnIdByAuthenticatedUser(columnDTO, columnDTO.whiteboardDTO());

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
        } catch (com.company.projectManager.common.exception.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/whiteboard/createNote", "/company/project/whiteboard/createNote", "/company/project/team/whiteboard/createNote"})
    public ResponseEntity<Object> createNote(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - NoteDTO noteDTO, BusinessUnitDTO*/) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String noteJSON = ow.writeValueAsString(requestBody.get("noteDTO"));
            String businessUnitJSON = ow.writeValueAsString(requestBody.get("businessUnitDTO"));

            ObjectMapper objectMapper = new ObjectMapper();

            noteService.createNoteByAuthenticatedUser(
                    objectMapper.readValue(noteJSON, NoteDTO.class),
                    objectMapper.readValue(businessUnitJSON, BusinessUnitDTO.class));

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
        } catch (ClassCastException | JsonProcessingException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/company/whiteboard/updateNote", "/company/project/whiteboard/updateNote", "/company/project/team/whiteboard/updateNote"})
    public ResponseEntity<Object> updateNote(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - NoteDTO noteDTO, BusinessUnitDTO*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String noteJSON = ow.writeValueAsString(requestBody.get("noteDTO"));
            String businessUnitJSON = ow.writeValueAsString(requestBody.get("businessUnitDTO"));

            ObjectMapper objectMapper = new ObjectMapper();

            noteService.updateNoteByAuthenticatedUser(
                    objectMapper.readValue(noteJSON, NoteDTO.class),
                    objectMapper.readValue(businessUnitJSON, BusinessUnitDTO.class));

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
        } catch (ClassCastException | JsonProcessingException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (com.company.projectManager.common.exception.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //fix that
    @DeleteMapping(value = {"/company/whiteboard/deleteNote", "/company/project/whiteboard/deleteNote", "/company/project/team/whiteboard/deleteNote"})
    public ResponseEntity<Object> deleteNote(@RequestBody Map<String, Object> requestBody /*Waiting for these 2 objects - NoteDTO noteDTO, BusinessUnitDTO*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String noteJSON = ow.writeValueAsString(requestBody.get("noteDTO"));
            String businessUnitJSON = ow.writeValueAsString(requestBody.get("businessUnitDTO"));

            ObjectMapper objectMapper = new ObjectMapper();

            noteService.deleteNoteByAuthenticatedUser(
                    objectMapper.readValue(noteJSON, NoteDTO.class),
                    objectMapper.readValue(businessUnitJSON, BusinessUnitDTO.class));

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
        } catch (ClassCastException | JsonProcessingException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (com.company.projectManager.common.exception.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
}
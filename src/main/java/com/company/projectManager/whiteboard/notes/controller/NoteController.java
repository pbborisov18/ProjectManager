package com.company.projectManager.whiteboard.notes.controller;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.service.NoteService;
import com.company.projectManager.common.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    //Why is this Post? Cuz something something GET requests with bodies is bad (and spring doesn't read it),
    //something something, expose the data in the url (no)
    //I'm either breaking one standard or the other. This one is worse to break but easier to implement. (You might say I'm a total idiot for doing this and I'd agree with you)
    @PostMapping(value = {"/company/whiteboard/notes", "/company/project/whiteboard/notes", "/company/project/team/whiteboard/notes"})
    public ResponseEntity<Object> getAllNotesOfColumn(@RequestBody @Valid ColumnDTO columnDTO){
        try {
            List<NoteDTO> notes = noteService.findAllNotesByColumn(columnDTO);

            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/whiteboard/createNote", "/company/project/whiteboard/createNote", "/company/project/team/whiteboard/createNote"})
    public ResponseEntity<Object> createNote(@RequestBody @Valid NoteDTO noteDTO) {
        try {
            noteService.createNote(noteDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSelectException | FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(value = {"/company/whiteboard/updateNote", "/company/project/whiteboard/updateNote", "/company/project/team/whiteboard/updateNote"})
    public ResponseEntity<Object> updateNote(@RequestBody @Valid NoteDTO noteDTO){
        try {
            noteService.updateNote(noteDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/company/whiteboard/updateNotes", "/company/project/whiteboard/updateNotes", "/company/project/team/whiteboard/updateNotes"})
    public ResponseEntity<Object> updateNotes(@RequestBody List<NoteDTO> notes){
        try {
            noteService.updateNotes(notes);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/company/whiteboard/deleteNote", "/company/project/whiteboard/deleteNote", "/company/project/team/whiteboard/deleteNote"})
    public ResponseEntity<Object> deleteNote(@RequestBody @Valid NoteDTO noteDTO){
        try {
            noteService.deleteNote(noteDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToSelectException | FailedToDeleteException | EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
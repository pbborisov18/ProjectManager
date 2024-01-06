package com.company.projectManager.whiteboard.notes.controller;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.notes.dto.ColumnBusinessUnitDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteBusinessUnitDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.dto.NotesBusinessUnitDTO;
import com.company.projectManager.whiteboard.notes.service.NoteService;
import com.company.projectManager.common.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    //TODO: Remove all the endpoints and leave only 1 per method

    //Why is this Post? Cuz something something GET requests with bodies is bad (and spring doesn't read it),
    //something something, expose the data in the url (no)
    //I'm either breaking one standard or the other. This one is worse to break but easier to implement. (You might say I'm a total idiot for doing this and I'd agree with you)
    @PostMapping(value = {"/company/whiteboard/notes", "/company/project/whiteboard/notes", "/company/project/team/whiteboard/notes"})
    @PreAuthorize("authorityCheck(#columnBUDTO.businessUnitDTO().id(), \"InteractWithWhiteboard\")")
    public ResponseEntity<Object> getAllNotesOfColumn(@RequestBody @Valid ColumnBusinessUnitDTO columnBUDTO){
        try {
            List<NoteDTO> notes = noteService.findAllNotesByColumn(columnBUDTO.columnDTO());

            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/company/whiteboard/createNote", "/company/project/whiteboard/createNote", "/company/project/team/whiteboard/createNote"})
    @PreAuthorize("authorityCheck(#noteBUDTO.businessUnitDTO().id(), \"InteractWithWhiteboard\")")
    public ResponseEntity<Object> createNote(@RequestBody @Valid NoteBusinessUnitDTO noteBUDTO) {
        try {
            noteService.createNote(noteBUDTO.noteDTO());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/company/whiteboard/updateNote", "/company/project/whiteboard/updateNote", "/company/project/team/whiteboard/updateNote"})
    @PreAuthorize("authorityCheck(#noteBUDTO.businessUnitDTO().id(), \"InteractWithWhiteboard\")")
    public ResponseEntity<Object> updateNote(@RequestBody @Valid NoteBusinessUnitDTO noteBUDTO){
        try {
            noteService.updateNote(noteBUDTO.noteDTO());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/company/whiteboard/updateNotes", "/company/project/whiteboard/updateNotes", "/company/project/team/whiteboard/updateNotes"})
    @PreAuthorize("authorityCheck(#notesBUDTO.businessUnitDTO().id(), \"InteractWithWhiteboard\")")
    public ResponseEntity<Object> updateNotes(@RequestBody @Valid NotesBusinessUnitDTO notesBUDTO){
        try {
            noteService.updateNotes(notesBUDTO.notes());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/company/whiteboard/deleteNote", "/company/project/whiteboard/deleteNote", "/company/project/team/whiteboard/deleteNote"})
    @PreAuthorize("authorityCheck(#noteBUDTO.businessUnitDTO().id(), \"InteractWithWhiteboard\")")
    public ResponseEntity<Object> deleteNote(@RequestBody @Valid NoteBusinessUnitDTO noteBUDTO){
        try {
            noteService.deleteNote(noteBUDTO.noteDTO());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
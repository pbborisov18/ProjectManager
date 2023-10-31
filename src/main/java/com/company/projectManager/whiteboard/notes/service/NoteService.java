package com.company.projectManager.whiteboard.notes.service;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;

import java.util.List;

public interface NoteService {

    List<NoteDTO> findAllNotesByColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, EntityNotFoundException, FailedToSaveException;

    void createNote(NoteDTO noteDTO) throws FailedToSelectException, UserUnauthenticatedException, FailedToSaveException;

    void updateNote(NoteDTO noteDTO) throws UserUnauthenticatedException, FailedToUpdateException;

    void updateNotes(List<NoteDTO> noteDTO) throws UserUnauthenticatedException, FailedToUpdateException;

    void deleteNote(NoteDTO noteDTO) throws FailedToDeleteException, FailedToSelectException, UserUnauthenticatedException, EntityNotFoundException;

}

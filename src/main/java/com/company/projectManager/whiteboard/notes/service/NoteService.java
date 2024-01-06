package com.company.projectManager.whiteboard.notes.service;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;

import java.util.List;

public interface NoteService {

    List<NoteDTO> findAllNotesByColumn(ColumnDTO columnDTO) throws EntityNotFoundException, FailedToSaveException;

    void createNote(NoteDTO noteDTO) throws FailedToSaveException;

    void updateNote(NoteDTO noteDTO) throws FailedToUpdateException;

    void updateNotes(List<NoteDTO> noteDTO) throws FailedToUpdateException;

    void deleteNote(NoteDTO noteDTO) throws FailedToDeleteException;

}

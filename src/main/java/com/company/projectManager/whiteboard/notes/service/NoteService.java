package com.company.projectManager.whiteboard.notes.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;

import java.util.List;

public interface NoteService {

    void saveNote(NoteDTO noteDTO) throws FailedToSaveException;

    void updateNote(NoteDTO noteDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteNote(NoteDTO noteDTO) throws FailedToDeleteException, EntityNotFoundException;

    void deleteNote (List<NoteDTO> noteDTOs) throws FailedToDeleteException, EntityNotFoundException;

    NoteDTO findNoteById(Long id) throws FailedToSelectException, EntityNotFoundException;

    List<NoteDTO> findAllNotes() throws FailedToSelectException, EntityNotFoundException;

    List<NoteDTO> findAllNotesByColumnId(ColumnDTO columnDTO) throws FailedToSelectException, EntityNotFoundException;

    List<NoteDTO> findAllNotesByColumnIdByAuthenticatedUser(ColumnDTO columnDTO, WhiteboardDTO whiteboardDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, EntityNotFoundException;

    void createNoteByAuthenticatedUser(NoteDTO noteDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserNotInBusinessUnitException, UserUnauthenticatedException, FailedToSaveException;

    void updateNoteByAuthenticatedUser(NoteDTO noteDTO, BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSelectException, FailedToUpdateException, EntityNotFoundException;

    void deleteNoteByAuthenticatedUser(NoteDTO noteDTO, BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException, FailedToSelectException, UserNotInBusinessUnitException, UserUnauthenticatedException, EntityNotFoundException;

}

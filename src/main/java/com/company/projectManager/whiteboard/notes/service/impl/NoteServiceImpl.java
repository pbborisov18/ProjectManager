package com.company.projectManager.whiteboard.notes.service.impl;

import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.entity.Note;
import com.company.projectManager.whiteboard.notes.mapper.NoteMapper;
import com.company.projectManager.whiteboard.notes.repository.NoteRepository;
import com.company.projectManager.whiteboard.notes.service.NoteService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    public List<NoteDTO> findAllNotesByColumn(ColumnDTO columnDTO) throws EntityNotFoundException, FailedToSaveException {
        try {
            List<Note> notes = noteRepository.findAllByColumnId(columnDTO.id());

            if(notes.isEmpty()) {
                throw new EntityNotFoundException("No notes found!");
            }

            return noteMapper.toDTO(notes);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful select!" + e.getMessage());
        }
    }

    public void createNote(NoteDTO noteDTO) throws FailedToSaveException {
        try {
            noteRepository.save(noteMapper.toEntity(noteDTO));
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to save!" + e.getMessage());
        }
    }

    public void updateNote(NoteDTO noteDTO) throws FailedToUpdateException {
        try {

            noteRepository.save(noteMapper.toEntity(noteDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update!" + e.getMessage());
        }
    }

    public void updateNotes(List<NoteDTO> notes) throws FailedToUpdateException {
        try {

            noteRepository.saveAll(noteMapper.toEntity(notes));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update!" + e.getMessage());
        }
    }

    public void deleteNote(NoteDTO noteDTO) throws FailedToDeleteException {
        try {
            //Will have to check what exception this throws
            //Hopefully caught by the bottom ones
            noteRepository.delete(noteMapper.toEntity(noteDTO));
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete!" + e.getMessage());
        }
    }

}

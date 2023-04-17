package com.company.projectManager.services;

import com.company.projectManager.DTOs.BusinessUnitDTO;
import com.company.projectManager.DTOs.ColumnDTO;
import com.company.projectManager.DTOs.NoteDTO;
import com.company.projectManager.DTOs.WhiteboardDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.mappers.NoteMapper;
import com.company.projectManager.models.Note;
import com.company.projectManager.models.User;
import com.company.projectManager.models.UserBusinessUnitRole;
import com.company.projectManager.repositories.NoteRepository;
import com.company.projectManager.repositories.UserRepository;
import com.company.projectManager.repositories.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public abstract class NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    NoteMapper noteMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    @Transactional
    public void saveNote(NoteDTO noteDTO) throws FailedToSaveException {
        try {
            Note note = noteMapper.toEntity(noteDTO);

            noteRepository.save(note);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    @Transactional
    public void updateNote(NoteDTO noteDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<Note> existingNote = noteRepository.findById(noteDTO.id());

            if(existingNote.isEmpty()) {
                throw new EntityNotFoundException("Note was not found!");
            }

            Note note = noteMapper.toEntity(noteDTO);

            noteRepository.save(note);


        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteNote(NoteDTO noteDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<Note> note = noteRepository.findById(noteDTO.id());

            if(note.isEmpty()) {
                throw new EntityNotFoundException("Note was not found!");
            }

            noteRepository.delete(note.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteNote(List<NoteDTO> noteDTOs) throws FailedToDeleteException, EntityNotFoundException {
        try {
            List<Note> notesToDelete = new ArrayList<>();

            for(NoteDTO noteDTO : noteDTOs) {
                Optional<Note> note = noteRepository.findById(noteDTO.id());

                if (note.isEmpty()) {
                    throw new EntityNotFoundException("Note was not found!");
                }

                notesToDelete.add(note.get());
            }

            noteRepository.deleteAll(notesToDelete);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    @Transactional
    public NoteDTO findNoteById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Note> note = noteRepository.findById(id);

            if(note.isEmpty()) {
                throw new EntityNotFoundException("Note was not found!");
            }

            return noteMapper.toDTO(note.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<NoteDTO> findAllNotes() throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Note> notes = (List<Note>) noteRepository.findAll();

            if(notes.size() == 0) {
                throw new EntityNotFoundException("No notes were found!");
            }

            return noteMapper.toDTO(notes);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<NoteDTO> findAllNotesByColumnId(ColumnDTO columnDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Note> note = (List<Note>) noteRepository.findAllByColumnId(columnDTO.id());

            if(note.isEmpty()) {
                throw new EntityNotFoundException("Notes not found!");
            }

            return noteMapper.toDTO(note);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<NoteDTO> findAllNotesByColumnIdByAuthenticatedUser(ColumnDTO columnDTO, WhiteboardDTO whiteboardDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), whiteboardDTO.id());

                if (userBusinessUnitRole.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
                }

                List<Note> note = (List<Note>) noteRepository.findAllByColumnId(columnDTO.id());

                if(note.isEmpty()) {
                    throw new EntityNotFoundException("Notes not found!");
                }

                return noteMapper.toDTO(note);

            }

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public void createNoteByAuthenticatedUser(NoteDTO noteDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserNotInBusinessUnitException, UserUnauthenticatedException, FailedToSaveException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }

            saveNote(noteDTO);


        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public void updateNoteByAuthenticatedUser(NoteDTO noteDTO, BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSelectException, FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }

            updateNote(noteDTO);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteNoteByAuthenticatedUser(NoteDTO noteDTO, BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException, FailedToSelectException, UserNotInBusinessUnitException, UserUnauthenticatedException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }

            deleteNote(noteDTO);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }
}

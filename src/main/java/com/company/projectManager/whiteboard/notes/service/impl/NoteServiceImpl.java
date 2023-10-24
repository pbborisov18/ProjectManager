package com.company.projectManager.whiteboard.notes.service.impl;

import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.entity.Note;
import com.company.projectManager.whiteboard.notes.mapper.NoteMapper;
import com.company.projectManager.whiteboard.notes.repository.NoteRepository;
import com.company.projectManager.whiteboard.notes.service.NoteService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

    private final UserRepository userRepository;

    private final UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, UserRepository userRepository, UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.userRepository = userRepository;
        this.usersBusinessUnitsRolesRepository = usersBusinessUnitsRolesRepository;
    }

    public List<NoteDTO> findAllNotesByColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, EntityNotFoundException, FailedToSaveException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), columnDTO.whiteboardDTO().id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }
            //-----------------

            List<Note> notes = noteRepository.findAllByColumnId(columnDTO.id());

            if(notes.isEmpty()) {
                throw new EntityNotFoundException("Notes not found!");
            }

            return noteMapper.toDTO(notes);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful select!" + e.getMessage());
        }
    }

    public void createNote(NoteDTO noteDTO) throws UserNotInBusinessUnitException, UserUnauthenticatedException, FailedToSaveException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), noteDTO.columnDTO().whiteboardDTO().id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }
            //-----------------

            noteRepository.save(noteMapper.toEntity(noteDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful select!" + e.getMessage());
        }
    }

    public void updateNote(NoteDTO noteDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToUpdateException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), noteDTO.columnDTO().whiteboardDTO().id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }
            //-----------------

            noteRepository.save(noteMapper.toEntity(noteDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful select!" + e.getMessage());
        }
    }

    //Probably can optimize this in some way but this is for future me
    public void updateNotes(List<NoteDTO> notes) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToUpdateException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), notes.get(0).columnDTO().whiteboardDTO().id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }
            //-----------------

            noteRepository.saveAll(noteMapper.toEntity(notes));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful select!" + e.getMessage());
        }
    }

    public void deleteNote(NoteDTO noteDTO) throws FailedToSelectException, UserNotInBusinessUnitException, UserUnauthenticatedException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), noteDTO.columnDTO().whiteboardDTO().id());

            if (userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }
            //-----------------

            noteRepository.delete(noteMapper.toEntity(noteDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

}

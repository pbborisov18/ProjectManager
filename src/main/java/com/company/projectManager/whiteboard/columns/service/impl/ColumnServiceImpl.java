package com.company.projectManager.whiteboard.columns.service.impl;

import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import com.company.projectManager.whiteboard.columns.mapper.ColumnMapper;
import com.company.projectManager.whiteboard.columns.repository.ColumnRepository;
import com.company.projectManager.whiteboard.columns.service.ColumnService;
import com.company.projectManager.whiteboard.notes.repository.NoteRepository;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnMapper columnMapper;

    private final ColumnRepository columnRepository;

    private final NoteRepository noteRepository;

    private final UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    private final UserRepository userRepository;

    public ColumnServiceImpl(ColumnMapper columnMapper, ColumnRepository columnRepository, NoteRepository noteRepository, UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository, UserRepository userRepository) {
        this.columnMapper = columnMapper;
        this.columnRepository = columnRepository;
        this.noteRepository = noteRepository;
        this.usersBusinessUnitsRolesRepository = usersBusinessUnitsRolesRepository;
        this.userRepository = userRepository;
    }

//    @Transactional
    public List<ColumnDTO> findAllColumnsByWhiteboard(WhiteboardDTO whiteboard) throws FailedToSelectException, UserUnauthenticatedException, EntityNotFoundException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }
            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), whiteboard.id());

            //-----------------

            List<Column> column = columnRepository.findAllByWhiteboardId(whiteboard.id());

            if (column.isEmpty()) {
                throw new EntityNotFoundException("Columns not found!");
            }

            return columnMapper.toDTO(column);


        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

//    @Transactional
    public void createColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, FailedToSaveException, UserNotAuthorizedException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), columnDTO.whiteboardDTO().id());


            //To be deleted when doing authorization anyway
//            if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//            }
            //-----------------

            columnRepository.save(columnMapper.toEntity(columnDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    //Currently not in use. Frontend needs to be updated. Perfect time for tests I'd say
    public void updateColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToUpdateException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), columnDTO.whiteboardDTO().id());


            //To be deleted when doing authorization anyway
//            if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//            }
            //-----------------

            columnRepository.save(columnMapper.toEntity(columnDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful select! " + e.getMessage());
        }
    }

    //Currently not in use. Frontend needs to be updated. Perfect time for tests I'd say
    public void updateColumns(List<ColumnDTO> columns)throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToUpdateException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), columns.get(0).whiteboardDTO().id());


            //To be deleted when doing authorization anyway
//            if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//            }
            //-----------------

            columnRepository.saveAll(columnMapper.toEntity(columns));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful select! " + e.getMessage());
        }
    }

    //Currently not in use. Frontend needs to be updated. Perfect time for tests I'd say
//    @Transactional
    public void deleteColumn(ColumnDTO columnDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotAuthorizedException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), columnDTO.whiteboardDTO().id());


            //To be deleted when doing authorization anyway
//            if (userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//            }
            //-----------------

            Column column = columnMapper.toEntity(columnDTO);
            //Can't cascade from the child. Making the relationship bidirectional will open way more work at the end. So just manual deletion :(
            noteRepository.deleteNotesByColumnId(column.getId());
            columnRepository.delete(column);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful select! " + e.getMessage());
        }
    }
}

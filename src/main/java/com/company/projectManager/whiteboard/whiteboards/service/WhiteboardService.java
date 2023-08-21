package com.company.projectManager.whiteboard.whiteboards.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.whiteboards.mapper.WhiteboardMapper;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.whiteboard.whiteboards.repository.WhiteboardRepository;
import com.company.projectManager.common.utils.RoleName;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.service.ColumnService;
import com.company.projectManager.whiteboard.notes.service.NoteService;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class WhiteboardService {

    @Autowired
    WhiteboardRepository whiteboardRepository;

    @Autowired
    WhiteboardMapper whiteboardMapper;

    @Autowired
    private UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    NoteService noteService;

    @Autowired
    private ColumnService columnService;

    @Transactional
    public void saveWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToSaveException {
        try {
            Whiteboard whiteboard = whiteboardMapper.toEntity(whiteboardDTO);

            whiteboardRepository.save(whiteboard);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    public void updateWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<Whiteboard> foundWhiteboard = whiteboardRepository.findById(whiteboardDTO.id());

            if(foundWhiteboard.isEmpty()) {
                throw new EntityNotFoundException("Whiteboard was not found");
            }

            whiteboardRepository.save(whiteboardMapper.toEntity(whiteboardDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update!" + e.getMessage());
        }
    }

    public void deleteWhiteboard(WhiteboardDTO whiteboardDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<Whiteboard> foundWhiteboard = whiteboardRepository.findById(whiteboardDTO.id());

            if(foundWhiteboard.isEmpty()) {
                throw new EntityNotFoundException("Whiteboard was not found");
            }

            whiteboardRepository.delete(foundWhiteboard.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    public WhiteboardDTO findWhiteboardById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Whiteboard> foundWhiteboard = whiteboardRepository.findById(id);

            if (foundWhiteboard.isEmpty()) {
                throw new EntityNotFoundException("Whiteboard was not found");
            }

            return whiteboardMapper.toDTO(foundWhiteboard.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    public WhiteboardDTO findWhiteboardByIdByAuthenticatedUser(BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSelectException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
               Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");

                } else {

                    if(businessUnitDTO.whiteboard() == null){
                        throw new EntityNotFoundException("Whiteboard doesn't exist");
                    }

                    Optional<Whiteboard> foundWhiteboard = whiteboardRepository.findById(businessUnitDTO.whiteboard().id());
                    if (foundWhiteboard.isEmpty()) {
                        throw new EntityNotFoundException("Whiteboard doesn't exist");
                    }

                    return whiteboardMapper.toDTO(foundWhiteboard.get());
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public void createWhiteboardWithAuthenticatedUser(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
                } else {
                    if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
                    }
                    //Assign the whiteboard to the businessUnit
                    userBusinessUnitRole.get().getBusinessUnit().setWhiteboard(whiteboardMapper.toEntity(whiteboardDTO));

                    //Save the business unit (in the process saving the whiteboard)
                    usersBusinessUnitsRolesRepository.save(userBusinessUnitRole.get());

                    WhiteboardDTO whiteboardWithId = new WhiteboardDTO(userBusinessUnitRole.get().getBusinessUnit().getWhiteboard().getId(), whiteboardDTO.name());

                    //Initialize the default columns
                    columnService.initializeDefaultColumns(whiteboardMapper.toEntity(whiteboardWithId));
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    public void deleteWhiteboardWithAuthenticatedUser(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSelectException, FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
                } else {
                    if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
                    }
                    //Update that Business Unit won't have a relationship with the whiteboard
                    userBusinessUnitRole.get().getBusinessUnit().setWhiteboard(null);

                    //Save the business unit
                    usersBusinessUnitsRolesRepository.save(userBusinessUnitRole.get());

                    //Delete Notes
                    for (ColumnDTO columnDTO : columnService.findAllColumnsByWhiteboardId(whiteboardDTO.id())) {
                        noteService.deleteNote(noteService.findAllNotesByColumnId(columnDTO));
                    }

                    //Delete Columns
                    columnService.deleteColumn(columnService.findAllColumnsByWhiteboardId(whiteboardDTO.id()));

                    //Delete Whiteboard
                    whiteboardRepository.delete(whiteboardMapper.toEntity(whiteboardDTO));
                    //Initialize the default columns
                    columnService.initializeDefaultColumns(whiteboardMapper.toEntity(whiteboardDTO));
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }




    public List<WhiteboardDTO> findAllWhiteboards() throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Whiteboard> foundWhiteboards = (List<Whiteboard>) whiteboardRepository.findAll();

            if(foundWhiteboards.size() == 0) {
                throw new EntityNotFoundException("Whiteboard was not found");
            }

            return whiteboardMapper.toDTO(foundWhiteboards);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

}

package com.company.projectManager.services;


import com.company.projectManager.DTOs.BusinessUnitDTO;
import com.company.projectManager.DTOs.ColumnDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.mappers.ColumnMapper;
import com.company.projectManager.models.*;
import com.company.projectManager.repositories.ColumnRepository;
import com.company.projectManager.repositories.UserRepository;
import com.company.projectManager.repositories.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.utils.RoleName;
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
public abstract class ColumnService {

    @Autowired
    ColumnMapper columnMapper;

    @Autowired
    ColumnRepository columnRepository;

    @Autowired
    private UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveColumn(ColumnDTO columnDTO) throws FailedToSaveException {
        try {
            Column column = columnMapper.toEntity(columnDTO);

            columnRepository.save(column);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    @Transactional
    public void updateColumn(ColumnDTO columnDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<Column> existingColumn = columnRepository.findById(columnDTO.id());

            if(existingColumn.isEmpty()){
                throw new EntityNotFoundException("Column was not found!");
            }

            columnRepository.save(columnMapper.toEntity(columnDTO));
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteColumn(ColumnDTO columnDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<Column> existingColumn = columnRepository.findById(columnDTO.id());

            if(existingColumn.isEmpty()){
                throw new EntityNotFoundException("Column was not found!");
            }

            columnRepository.delete(existingColumn.get());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteColumn(List<ColumnDTO> columnDTOs) throws FailedToDeleteException, EntityNotFoundException {
        try {
            List<Column> columnsToDelete = new ArrayList<>();

            for(ColumnDTO columnDTO : columnDTOs){
                Optional<Column> existingColumn = columnRepository.findById(columnDTO.id());

                if(existingColumn.isEmpty()){
                    throw new EntityNotFoundException("Column was not found!");
                }

                columnsToDelete.add(existingColumn.get());
            }

            columnRepository.deleteAll(columnsToDelete);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    @Transactional
    public ColumnDTO findColumnById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Column> column = columnRepository.findById(id);

            if(column.isEmpty()){
                throw new EntityNotFoundException("Column was not found!");
            }

            return columnMapper.toDTO(column.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<ColumnDTO> findAllColumns() throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Column> column = (List<Column>) columnRepository.findAll();

            if(column.isEmpty()){
                throw new EntityNotFoundException("Column was not found!");
            }

            return columnMapper.toDTO(column);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<ColumnDTO> findAllColumnsByWhiteboardId(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Column> column = columnRepository.findAllByWhiteboardId(id);

            if(column.isEmpty()){
                throw new EntityNotFoundException("Columns not found!");
            }

            return columnMapper.toDTO(column);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public List<ColumnDTO> findAllColumnsByWhiteboardIdByAuthenticatedUser(Long id) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isEmpty()) {
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitWhiteboardId(user.get().getId(), id);

                if (userBusinessUnitRole.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");

                } else {
                    List<Column> column = columnRepository.findAllByWhiteboardId(id);

                    if (column.isEmpty()) {
                        throw new EntityNotFoundException("Columns not found!");
                    }

                    return columnMapper.toDTO(column);
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }



    @Transactional
    public void createColumnByAuthenticatedUser(ColumnDTO columnDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSaveException, UserNotAuthorizedException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
                }
                if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
                    throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
                }

                columnRepository.save(columnMapper.toEntity(columnDTO));

            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteColumnByAuthenticatedUser(ColumnDTO columnDTO, BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, UserNotAuthorizedException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
                }
                if (userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
                    throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
                }

                columnRepository.delete(columnMapper.toEntity(columnDTO));

            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful select! " + e.getMessage());
        }
    }

//    @Transactional
//    public void createColumnByAuthenticatedUser(ColumnDTO columnDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException {
//        try {
//            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
//
//            if(user.isEmpty()){
//                throw new UserUnauthenticatedException("User isn't authenticated!");
//            } else {
//                Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());
//
//                if (userBusinessUnitRole.isEmpty()) {
//                    throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
//                } else {
//
//                }
//            }
//        } catch (ConstraintViolationException | DataAccessException e) {
//            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
//        }
//    }

    public void initializeDefaultColumns(Whiteboard whiteboard) throws FailedToSaveException {
        try {
            Column column1 = new Column(null, "To do", whiteboard);
            Column column2 = new Column(null, "Doing", whiteboard);
            Column column3 = new Column(null, "Testing", whiteboard);
            Column column4 = new Column(null, "Done", whiteboard);

            columnRepository.saveAll(List.of(column1, column2, column3, column4));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

}

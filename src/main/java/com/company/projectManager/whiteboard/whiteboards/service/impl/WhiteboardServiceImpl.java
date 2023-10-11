package com.company.projectManager.whiteboard.whiteboards.service.impl;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.repository.BusinessUnitRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import com.company.projectManager.whiteboard.columns.repository.ColumnRepository;
import com.company.projectManager.whiteboard.notes.repository.NoteRepository;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import com.company.projectManager.whiteboard.whiteboards.mapper.WhiteboardMapper;
import com.company.projectManager.whiteboard.whiteboards.repository.WhiteboardRepository;
import com.company.projectManager.whiteboard.whiteboards.service.WhiteboardService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WhiteboardServiceImpl implements WhiteboardService {

    private final UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    private final UserRepository userRepository;



    private final BusinessUnitRepository businessUnitRepository;

    private final WhiteboardRepository whiteboardRepository;

    private final WhiteboardMapper whiteboardMapper;

    private final NoteRepository noteRepository;

    private final ColumnRepository columnRepository;

    public WhiteboardServiceImpl(WhiteboardRepository whiteboardRepository, WhiteboardMapper whiteboardMapper, UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository, UserRepository userRepository, BusinessUnitRepository businessUnitRepository, NoteRepository noteRepository, ColumnRepository columnRepository) {
        this.whiteboardRepository = whiteboardRepository;
        this.whiteboardMapper = whiteboardMapper;
        this.usersBusinessUnitsRolesRepository = usersBusinessUnitsRolesRepository;
        this.userRepository = userRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.noteRepository = noteRepository;
        this.columnRepository = columnRepository;
    }

    public WhiteboardDTO findWhiteboard(BusinessUnitDTO businessUnitDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSelectException, EntityNotFoundException  {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }
            Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBusinessUnitRole.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }
            //-----------------

            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }

            if(businessUnit.get().getWhiteboard() == null){
                throw new EntityNotFoundException("Whiteboard doesn't exist");
            }

            return whiteboardMapper.toDTO(businessUnit.get().getWhiteboard());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBusinessUnitRole.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }

//            if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//            }
            //-----------------

            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }


            Whiteboard whiteboard = whiteboardMapper.toEntity(whiteboardDTO);
            //????????????????????????????
            //There is transactional wtf. Why do I need to save this before the columns?
            //The initializeDefaultColumns uses a Transactional MANDATORY. So it doesn't make a new transaction
            //WTF
            whiteboardRepository.save(whiteboard);

            //Initialize the default columns
            initializeDefaultColumns(whiteboard);

            //Assign the whiteboard to the businessUnit
            businessUnit.get().setWhiteboard(whiteboard);

            //Save the business unit and whiteboard cuz cascade
            businessUnitRepository.save(businessUnit.get());



        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    @Transactional
    public void createWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO, List<ColumnDTO> columns) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException {
        //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
        Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(user.isEmpty()){
            throw new UserUnauthenticatedException("User isn't authenticated!");
        }

        Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

        if(userBusinessUnitRole.isEmpty()){
            throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
        }

//        if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//            throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//        }
        //-----------------

        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

        if(businessUnit.isEmpty()){
            throw new EntityNotFoundException("BusinessUnit not found");
        }

        Whiteboard whiteboard = whiteboardMapper.toEntity(whiteboardDTO);

        whiteboardRepository.save(whiteboard);

        List<Column> columnEntities = new ArrayList<>();

        //Why not use mapper? Cuz validations cuz column's will have the whiteboard set as null
        for (ColumnDTO column : columns){
            Column columnEntity = new Column();
            columnEntity.setName(column.name());
            columnEntity.setPosition(column.position());
            columnEntity.setWhiteboard(whiteboard);

            columnEntities.add(columnEntity);
        }

        //Since hibernate isn't stupid it will only execute this after the whiteboard is already in the db
        columnRepository.saveAll(columnEntities);

        //Assign the whiteboard to the businessUnit
        businessUnit.get().setWhiteboard(whiteboard);

        //Save the business unit and whiteboard cuz cascade
        //Cascade doesn't work for some reason :O
        businessUnitRepository.save(businessUnit.get());

    }

    @Transactional
    public void deleteWhiteboard(WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO) throws FailedToSaveException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBusinessUnitRole = usersBusinessUnitsRolesRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBusinessUnitRole.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't a part of the business unit!");
            }

//            if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                throw new UserNotAuthorizedException("User doesn't have the necessary permissions");
//            }
            //-----------------

            Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(businessUnitDTO.id());

            if(businessUnit.isEmpty()){
                throw new EntityNotFoundException("BusinessUnit not found");
            }

            //Since the method is transactional the order we do these in shouldn't matter.
            //Since whiteboard doesn't have any relationships (which it knows about) everything has to be handled manually
            //Basically all relationships are backwards for easier time with the mapper

            //Update that Business Unit won't have a relationship with the whiteboard
            businessUnit.get().setWhiteboard(null);

            //Save the business unit
            businessUnitRepository.save(businessUnit.get());

            //Delete Notes
            for (Column column : columnRepository.findAllByWhiteboardId(whiteboardDTO.id())) {
                noteRepository.deleteNotesByColumnId(column.getId());
            }

            //Delete Columns
            columnRepository.deleteAllByWhiteboardId(whiteboardDTO.id());

            //Delete Whiteboard
            whiteboardRepository.delete(whiteboardMapper.toEntity(whiteboardDTO));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    //Why is this public? Cuz transactional doesn't work on private methods
    @Transactional(Transactional.TxType.MANDATORY)
    public void initializeDefaultColumns(Whiteboard whiteboard) throws FailedToSaveException {
        try {
            Column column1 = new Column(null, "To do", whiteboard, 1L);
            Column column2 = new Column(null, "Doing", whiteboard, 2L);
            Column column3 = new Column(null, "Testing", whiteboard, 3L);
            Column column4 = new Column(null, "Done", whiteboard, 4L);

            //Since hibernate isn't stupid it will only execute this after the whiteboard is already in the db
            columnRepository.saveAll(List.of(column1, column2, column3, column4));

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }
}

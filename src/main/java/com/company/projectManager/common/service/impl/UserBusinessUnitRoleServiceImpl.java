package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.mapper.RoleMapper;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.mapper.UsersBusinessUnitsRolesMapper;
import com.company.projectManager.common.repository.BusinessUnitRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.service.BusinessUnitService;
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
import com.company.projectManager.common.utils.TypeName;
//import com.company.projectManager.common.utils.UserBusinessUnitRoleId;
import com.company.projectManager.invitation.entity.Invite;
import com.company.projectManager.invitation.repository.InviteRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserBusinessUnitRoleServiceImpl implements UserBusinessUnitRoleService {

    private final UsersBusinessUnitsRolesRepository userBURoleRepository;

    private final UsersBusinessUnitsRolesMapper userBURoleMapper;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BusinessUnitMapper businessUnitMapper;

    private final RoleMapper roleMapper;

    private final BusinessUnitService businessUnitService;

    private final BusinessUnitRepository businessUnitRepository;

    private final InviteRepository inviteRepository;

    public UserBusinessUnitRoleServiceImpl(UsersBusinessUnitsRolesRepository userBURoleRepository, UsersBusinessUnitsRolesMapper userBURoleMapper, UserRepository userRepository, UserMapper userMapper, BusinessUnitMapper businessUnitMapper, RoleMapper roleMapper, BusinessUnitService businessUnitService, BusinessUnitRepository businessUnitRepository, InviteRepository inviteRepository) {
        this.userBURoleRepository = userBURoleRepository;
        this.userBURoleMapper = userBURoleMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.businessUnitMapper = businessUnitMapper;
        this.roleMapper = roleMapper;
        this.businessUnitService = businessUnitService;
        this.businessUnitRepository = businessUnitRepository;
        this.inviteRepository = inviteRepository;
    }

    @Transactional
    public void saveUserBURole(UserBusinessUnitRoleDTO userBURole) throws FailedToSaveException {
        try {
            userBURoleRepository.save(userBURoleMapper.toEntity(userBURole));
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteUserBURole(UserBusinessUnitRoleDTO userBuRole) throws FailedToDeleteException, EntityNotFoundException {
        try{

            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(userBuRole.user().id(), userBuRole.businessUnit().id());

            if(userBURoleEntity.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            userBURoleRepository.delete(userBURoleEntity.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllCompaniesByUserId(Long userId) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserIdAndBusinessUnitType(userId, TypeName.COMPANY);

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findCompaniesByAuthenticatedUser() throws UserUnauthenticatedException, FailedToSelectException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isPresent()) {
                return findAllCompaniesByUserId(user.get().getId());
            } else {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            }

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public void createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException {
        try {
            Optional<User> user = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                UserDTO userDTO = userMapper.toUserDTO(user.get());

                //TODO: This has to be reworked
                UserBusinessUnitRoleDTO userBUrole = new UserBusinessUnitRoleDTO(
                        null,
                        userDTO,
                        companyDTO,
                        null
                );

                saveUserBURole(userBUrole);
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void updateCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSaveException, FailedToUpdateException, UserNotAuthorizedException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), companyDTO.id());

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
//                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
//                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
//                    } else {
                        UserBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserBusinessUnitRoleDTO(
                                null,
                                userMapper.toUserDTO(user.get()),
                                companyDTO,
                                roleMapper.toDTO(userBURoleEntity.get().getRole())
                        );

                        saveUserBURole(userWithPassBusinessUnitRoleDTO);
//                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update! " + e.getMessage());
        }
    }

    @Transactional
    public void leaveCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, FailedToLeaveException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), companyDTO.id());

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    UserBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserBusinessUnitRoleDTO(
                            null,
                            userMapper.toUserDTO(user.get()),
                            companyDTO,
                            roleMapper.toDTO(userBURoleEntity.get().getRole())
                    );
                    //Deletes the current relationship in the table
                    deleteUserBURole(userWithPassBusinessUnitRoleDTO);

                    //Deletes the relationship with all children Projects and Teams under the company left
                    userBURoleRepository.deleteAll(userBURoleRepository.findAllByUserIdAndBusinessUnitCompanyId(user.get().getId(), companyDTO.id()));
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToLeaveException("Unsuccessful leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToDeleteException, FailedToSelectException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), companyDTO.id());

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    //TODO: Fix with the custom authorization
//                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
//                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
//                    } else {
                        //Delete the relationship before the businessUnit
                        userBURoleRepository.deleteAll(userBURoleRepository.findAllByBusinessUnitId(companyDTO.id()));

                        //Delete all children invites
                        for (UserBusinessUnitRole BUDTO : userBURoleRepository.findAllByUserIdAndBusinessUnitCompanyId(user.get().getId(), companyDTO.id())) {
                            deleteAllInvitesByBusinessUnit(userBURoleMapper.toDTO(BUDTO).businessUnit());
                        }

                        //delete all invites for the company
                        deleteAllInvitesByBusinessUnit(companyDTO);


                        //Delete all relationships of the children projects and teams of the company
                        userBURoleRepository.deleteAll(
                                userBURoleRepository.findAllByUserIdAndBusinessUnitCompanyId(user.get().getId(), companyDTO.id()));
                        try {
                            //Delete all children projects and teams of the company
                            businessUnitRepository.deleteAll(
                                    businessUnitMapper.toBusinessUnitEntities(
                                            businessUnitService.findBusinessUnitsByCompany(companyDTO)));
                        } catch (EntityNotFoundException e) {
                            //No kids to delete
                        }

                        //Delete the company
                        businessUnitService.deleteBusinessUnit(companyDTO);

//                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {

            throw new FailedToDeleteException("Unsuccessful select! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllProjectsByUserIdAndCompany(Long userId, CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserIdAndBusinessUnitTypeAndBusinessUnitCompanyId(userId, TypeName.PROJECT, companyDTO.id());

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSelectException, UserNotInBusinessUnitException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), companyDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else{
                    return findAllProjectsByUserIdAndCompany(user.get().getId(), companyDTO);
                }
            }

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                UserDTO userDTO = userMapper.toUserDTO(user.get());

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(userDTO.id(), projectDTO.company().id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    //TODO: Fix with the custom authorization
//                    if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
//                    } else {
                    //TODO: This has to be reworked
                        UserBusinessUnitRoleDTO userBUrole = new UserBusinessUnitRoleDTO(
                                null,
                                userDTO,
                                projectDTO,
                                null
                        );

                        saveUserBURole(userBUrole);
//                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void updateProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException {
        createProject(projectDTO);
    }

    @Transactional
    public void leaveProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, FailedToLeaveException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                UserDTO userDTO = userMapper.toUserDTO(user.get());

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(userDTO.id(), projectDTO.company().id());

                if(userBusinessUnitRole.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {

                    UserBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserBusinessUnitRoleDTO(
                            null,
                            userMapper.toUserDTO(user.get()),
                            projectDTO,
                            roleMapper.toDTO(userBusinessUnitRole.get().getRole())
                    );
                    //Deletes the current relationship in the table
                    deleteUserBURole(userWithPassBusinessUnitRoleDTO);

                    //Deletes the relationship with all children Teams under the Project
                    userBURoleRepository.deleteAll(userBURoleRepository.findAllByUserIdAndBusinessUnitProjectId(user.get().getId(), projectDTO.id()));
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToLeaveException("Unsuccessful leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSelectException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), projectDTO.id());

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the project!");
                } else {
                    //TODO: Fix with the custom authorization
//                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
//                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
//                    } else {
                        //Delete the relationship before the businessUnit
                        userBURoleRepository.deleteAll(
                                userBURoleRepository.findAllByBusinessUnitId(projectDTO.id()));

                        //Delete all children invites
                        for (UserBusinessUnitRole BUDTO : userBURoleRepository.findAllByUserIdAndBusinessUnitProjectId(user.get().getId(), projectDTO.id())) {
                            deleteAllInvitesByBusinessUnit(userBURoleMapper.toDTO(BUDTO).businessUnit());
                        }

                        //delete all invites for the company
                        deleteAllInvitesByBusinessUnit(projectDTO);

                        //Delete all relationships of the children teams of the project
                        userBURoleRepository.deleteAll(
                                userBURoleRepository.findAllByUserIdAndBusinessUnitProjectId(user.get().getId(), projectDTO.id()));
                        try{
                            //Delete all children teams of the project
                            businessUnitRepository.deleteAll(
                                    businessUnitMapper.toBusinessUnitEntities(
                                            businessUnitService.findBusinessUnitsByProject(projectDTO)));
                        } catch (EntityNotFoundException e) {
                            //No kids to delete
                        }

                        //Delete the project
                        businessUnitService.deleteBusinessUnit(projectDTO);
//                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllTeamsByUserIdAndProject(Long userId, ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserIdAndBusinessUnitTypeAndBusinessUnitProjectId(userId, TypeName.TEAM, projectDTO.id());

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSelectException, UserNotInBusinessUnitException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), projectDTO.id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the parent project!");
                } else{
                    return findAllTeamsByUserIdAndProject(user.get().getId(), projectDTO);
                }
            }

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public void createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                UserDTO userDTO = userMapper.toUserDTO(user.get());

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(userDTO.id(), teamDTO.project().id());

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the parent project!");
                } else {
                    //TODO: Fix with the custom authorization
//                    if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
//                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
//                    } else {
                    //TODO: This has to be reworked
                        UserBusinessUnitRoleDTO userBUrole = new UserBusinessUnitRoleDTO(
                                null,
                                userDTO,
                                teamDTO,
                                null
                        );

                        saveUserBURole(userBUrole);

//                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void updateTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException {
        createTeam(teamDTO);
    }

    @Transactional
    public void leaveTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, FailedToLeaveException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                UserDTO userDTO = userMapper.toUserDTO(user.get());

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(userDTO.id(), teamDTO.project().id());

                if(userBusinessUnitRole.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the team!");
                } else {

                    UserBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserBusinessUnitRoleDTO(
                            null,
                            userMapper.toUserDTO(user.get()),
                            teamDTO,
                            roleMapper.toDTO(userBusinessUnitRole.get().getRole())
                    );
                    //Deletes the current relationship in the table
                    deleteUserBURole(userWithPassBusinessUnitRoleDTO);

                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToLeaveException("Unsuccessful leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), teamDTO.id());

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the team!");
                } else {
                    //TODO: Fix with the custom authorization
//                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
//                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
//                    } else {
                        //Delete the relationship before the businessUnit
                        userBURoleRepository.deleteAll(userBURoleRepository.findAllByBusinessUnitId(teamDTO.id()));

                        //Delete invites
                        deleteAllInvitesByBusinessUnit(teamDTO);

                        //Delete the project
                        businessUnitService.deleteBusinessUnit(teamDTO);
//                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////


    //?? No idea why this isn't used
    //TODO: fix
    @Transactional
    public UserNoPassBusinessUnitRoleDTO findById(Long userId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(userId);

            if(userBURoleEntity.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleEntity.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    //Useless cuz in no page will they all be displayed
    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllByUserId(Long userId) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserId(userId);

            if (userBURoleList.isEmpty()) {
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    //I'm directly calling the repository where it's needed lol
    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllByBusinessUnitId(Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByBusinessUnitId(businessUnitId);

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    //I guess useless filter
    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllByRoleIdAndBusinessUnitId(Long roleId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByRoleIdAndBusinessUnitId(roleId, businessUnitId);

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    //I'm directly calling the repository where it's needed lol
    @Transactional
    public UserNoPassBusinessUnitRoleDTO findByUserIdAndBusinessUnitId(Long userId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<UserBusinessUnitRole> userBURole = userBURoleRepository.findByUserIdAndBusinessUnitId(userId, businessUnitId);

            if(userBURole.isPresent()){
                return userBURoleMapper.toDTO(userBURole.get());
            } else {
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException {
        try {
            List<Invite> invites = inviteRepository.findAllByBusinessUnitId(businessUnitDTO.id());

            if(invites.isEmpty()){
                return;
            }

            inviteRepository.deleteAll(invites);
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

}

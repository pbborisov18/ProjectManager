package com.company.projectManager.services;

import com.company.projectManager.DTOs.*;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.mappers.BusinessUnitMapper;
import com.company.projectManager.mappers.RoleMapper;
import com.company.projectManager.mappers.UserMapper;
import com.company.projectManager.mappers.UsersBusinessUnitsRolesMapper;
import com.company.projectManager.models.User;
import com.company.projectManager.models.UserBusinessUnitRole;
import com.company.projectManager.exceptions.EntityNotFoundException;
import com.company.projectManager.repositories.BusinessUnitRepository;
import com.company.projectManager.repositories.UserRepository;
import com.company.projectManager.repositories.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.utils.RoleName;
import com.company.projectManager.utils.TypeName;
import com.company.projectManager.utils.UserBusinessUnitRoleId;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public abstract class UserBusinessUnitRoleService {

    @Autowired
    UsersBusinessUnitsRolesRepository userBURoleRepository;

    @Autowired
    UsersBusinessUnitsRolesMapper userBURoleMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BusinessUnitMapper businessUnitMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    BusinessUnitService businessUnitService;

    @Autowired
    BusinessUnitRepository businessUnitRepository;

    @Autowired
    InviteService inviteService;

    @Transactional
    public void saveUserBURole(UserWithPassBusinessUnitRoleDTO userBURole) throws FailedToSaveException {
        try {
            userBURoleRepository.save(userBURoleMapper.toEntity(userBURole));
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteUserBURole(UserWithPassBusinessUnitRoleDTO userBuRole) throws FailedToDeleteException, EntityNotFoundException {
        try{
            UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(userBuRole.user().id(), userBuRole.businessUnit().id());

            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

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
    public List<UserBusinessUnitRoleDTO> findAllCompaniesByUserId(Long userId) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserIdAndBusinessUnitTypeName(userId, TypeName.COMPANY);

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserBusinessUnitRoleDTO> findCompaniesByAuthenticatedUser() throws UserUnauthenticatedException, FailedToSelectException, EntityNotFoundException {
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

                UserWithPassBusinessUnitRoleDTO userBUrole = new UserWithPassBusinessUnitRoleDTO(
                        userDTO,
                        companyDTO,
                        new RoleDTO(3L, RoleName.MANAGER)
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
                UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(user.get().getId(), companyDTO.id());
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                    } else {
                        UserWithPassBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserWithPassBusinessUnitRoleDTO(
                                userMapper.toUserDTO(user.get()),
                                companyDTO,
                                roleMapper.toDTO(userBURoleEntity.get().getRole())
                        );

                        saveUserBURole(userWithPassBusinessUnitRoleDTO);
                    }
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
                UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(user.get().getId(), companyDTO.id());
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    UserWithPassBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserWithPassBusinessUnitRoleDTO(
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
                UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(user.get().getId(), companyDTO.id());
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                    } else {
                        //Delete the relationship before the businessUnit
                        userBURoleRepository.deleteAll(userBURoleRepository.findAllByBusinessUnitId(companyDTO.id()));

                        //Delete all children invites
                        for (UserBusinessUnitRole BUDTO : userBURoleRepository.findAllByUserIdAndBusinessUnitCompanyId(user.get().getId(), companyDTO.id())) {
                            inviteService.deleteAllInvitesByBusinessUnit(userBURoleMapper.toDTO(BUDTO).businessUnit());
                        }

                        //delete all invites for the company
                        inviteService.deleteAllInvitesByBusinessUnit(companyDTO);


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

                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {

            throw new FailedToDeleteException("Unsuccessful select! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllProjectsByUserIdAndCompany(Long userId, CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserIdAndBusinessUnitTypeNameAndBusinessUnitCompanyId(userId, TypeName.PROJECT, companyDTO.id());

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSelectException, UserNotInBusinessUnitException, EntityNotFoundException {
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

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findById(new UserBusinessUnitRoleId(userDTO.id(), projectDTO.company().id()));

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {
                    if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                    } else {
                        UserWithPassBusinessUnitRoleDTO userBUrole = new UserWithPassBusinessUnitRoleDTO(
                                userDTO,
                                projectDTO,
                                new RoleDTO(3L, RoleName.MANAGER)
                        );

                        saveUserBURole(userBUrole);
                    }
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

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findById(new UserBusinessUnitRoleId(userDTO.id(), projectDTO.company().id()));

                if(userBusinessUnitRole.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the company!");
                } else {

                    UserWithPassBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserWithPassBusinessUnitRoleDTO(
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
                UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(user.get().getId(), projectDTO.id());
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the project!");
                } else {
                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                    } else {
                        //Delete the relationship before the businessUnit
                        userBURoleRepository.deleteAll(
                                userBURoleRepository.findAllByBusinessUnitId(projectDTO.id()));

                        //Delete all children invites
                        for (UserBusinessUnitRole BUDTO : userBURoleRepository.findAllByUserIdAndBusinessUnitProjectId(user.get().getId(), projectDTO.id())) {
                            inviteService.deleteAllInvitesByBusinessUnit(userBURoleMapper.toDTO(BUDTO).businessUnit());
                        }

                        //delete all invites for the company
                        inviteService.deleteAllInvitesByBusinessUnit(projectDTO);

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
                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllTeamsByUserIdAndProject(Long userId, ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<UserBusinessUnitRole> userBURoleList = userBURoleRepository.findAllByUserIdAndBusinessUnitTypeNameAndBusinessUnitProjectId(userId, TypeName.TEAM, projectDTO.id());

            if(userBURoleList.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleList);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSelectException, UserNotInBusinessUnitException, EntityNotFoundException {
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

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findById(new UserBusinessUnitRoleId(userDTO.id(), teamDTO.project().id()));

                if(userBusinessUnitRole.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't a part of the parent project!");
                } else {
                    if(userBusinessUnitRole.get().getRole().getName() != RoleName.MANAGER){
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                    } else {
                        UserWithPassBusinessUnitRoleDTO userBUrole = new UserWithPassBusinessUnitRoleDTO(
                                userDTO,
                                teamDTO,
                                new RoleDTO(3L, RoleName.MANAGER)
                        );

                        saveUserBURole(userBUrole);

                    }
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

                Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findById(new UserBusinessUnitRoleId(userDTO.id(), teamDTO.project().id()));

                if(userBusinessUnitRole.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the team!");
                } else {

                    UserWithPassBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO = new UserWithPassBusinessUnitRoleDTO(
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
                UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(user.get().getId(), teamDTO.id());
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

                if(userBURoleEntity.isEmpty()) {
                    throw new UserNotInBusinessUnitException("User isn't a part of the team!");
                } else {
                    if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
                        throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                    } else {
                        //Delete the relationship before the businessUnit
                        userBURoleRepository.deleteAll(userBURoleRepository.findAllByBusinessUnitId(teamDTO.id()));

                        //Delete invites
                        inviteService.deleteAllInvitesByBusinessUnit(teamDTO);

                        //Delete the project
                        businessUnitService.deleteBusinessUnit(teamDTO);
                    }
                }
            }
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////


    @Transactional
    public UserBusinessUnitRoleDTO findById(Long userId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
        try {
            UserBusinessUnitRoleId id = new UserBusinessUnitRoleId(userId, businessUnitId);
            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findById(id);

            if(userBURoleEntity.isEmpty()){
                throw new EntityNotFoundException("UserBusinessUnitRole does not exist!");
            }

            return userBURoleMapper.toDTO(userBURoleEntity.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllByUserId(Long userId) throws FailedToSelectException, EntityNotFoundException {
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

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllByBusinessUnitId(Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
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

    @Transactional
    public List<UserBusinessUnitRoleDTO> findAllByRoleIdAndBusinessUnitId(Long roleId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
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

    @Transactional
    public UserBusinessUnitRoleDTO findByUserIdAndBusinessUnitId(Long userId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException {
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


}

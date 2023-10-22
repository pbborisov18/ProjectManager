package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.RoleMapper;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.mapper.UsersBusinessUnitsRolesMapper;
import com.company.projectManager.common.repository.BusinessUnitRepository;
import com.company.projectManager.common.repository.RoleRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.invitation.entity.Invite;
import com.company.projectManager.invitation.repository.InviteRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserBusinessUnitRoleServiceImpl implements UserBusinessUnitRoleService {

    private final UsersBusinessUnitsRolesRepository userBURoleRepository;

    private final UsersBusinessUnitsRolesMapper userBURoleMapper;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final BusinessUnitRepository businessUnitRepository;

    private final InviteRepository inviteRepository;

    private final RoleRepository roleRepository;

    public UserBusinessUnitRoleServiceImpl(UsersBusinessUnitsRolesRepository userBURoleRepository, UsersBusinessUnitsRolesMapper userBURoleMapper, UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper, BusinessUnitRepository businessUnitRepository, InviteRepository inviteRepository,
                                           RoleRepository roleRepository) {
        this.userBURoleRepository = userBURoleRepository;
        this.userBURoleMapper = userBURoleMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.businessUnitRepository = businessUnitRepository;
        this.inviteRepository = inviteRepository;
        this.roleRepository = roleRepository;
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

    public List<UserNoPassBusinessUnitRoleDTO> findAllCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException {
        try {
            //It's safe to use the id here for it to be more performant. You can get it from the authorities, 2nd "id" (Check out the class SecurityIds)
            //Will do if more perf is needed but I doubt it would make a big difference for the life of this
            //This is for every single method here that needs to do something using the authenticated user
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            List<UserNoPassBusinessUnitRoleDTO> userBURoles = userBURoleMapper.toDTO(
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitType(email, TypeName.COMPANY));

            if(userBURoles.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBURoles;

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
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
            }

            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), companyDTO.id());

            if(userBURoleEntity.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't a part of the company!");
            }

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

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update! " + e.getMessage());
        }
    }

    public void leaveCompany(CompanyDTO companyDTO) throws FailedToLeaveException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //Delete all userBURole entries
            List<UserBusinessUnitRole> userBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitId(email, companyDTO.id());

            userBURoleRepository.deleteAll(userBURoles);

            //Delete all child userBURole entries
            List<UserBusinessUnitRole> childUserBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitCompanyId(email, companyDTO.id());

            userBURoleRepository.deleteAll(childUserBURoles);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToLeaveException("Failed to leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCompany(CompanyDTO companyDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez change as I don't rely on cascading for deletion
            //But isn't worth the extra effort for this project

            //Delete all invites for the company
            inviteRepository.deleteAllByBusinessUnitId(companyDTO.id());

            //TODO: think about this more (prob will need a rework)
            //Delete all roles in the company
            roleRepository.deleteAllByBusinessUnitId(companyDTO.id());

            //Delete userBURoles
            userBURoleRepository.deleteAllByBusinessUnitId(companyDTO.id());

            //Here's a good idea to call children but they are taking only 1 dto. I'll just make a helper method.
            deleteAllProjects(businessUnitRepository.findAllByCompanyId(companyDTO.id()));

            //Finally delete company
            businessUnitRepository.deleteById(companyDTO.id());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            String email  = SecurityContextHolder.getContext().getAuthentication().getName();

            List<UserNoPassBusinessUnitRoleDTO> userBURoles = userBURoleMapper.toDTO(
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitCompanyIdAndBusinessUnitType(email, companyDTO.id(), TypeName.PROJECT));

            if(userBURoles.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBURoles;

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            }

            UserDTO userDTO = userMapper.toUserDTO(user.get());

            Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(userDTO.id(), projectDTO.company().id());

            if(userBusinessUnitRole.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't a part of the company!");
            }

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
//

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void updateProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException {
        createProject(projectDTO);
    }

    @Transactional
    public void leaveProject(ProjectDTO projectDTO) throws FailedToLeaveException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //Delete all userBURole entries
            List<UserBusinessUnitRole> userBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitId(email, projectDTO.id());

            userBURoleRepository.deleteAll(userBURoles);

            //Delete all child userBURole entries
            List<UserBusinessUnitRole> childUserBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitProjectId(email, projectDTO.id());

            userBURoleRepository.deleteAll(childUserBURoles);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToLeaveException("Unsuccessful leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProject(ProjectDTO projectDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez change as I don't rely on cascading for deletion
            //But isn't worth the extra effort for this project

            //Delete all invites for the project
            inviteRepository.deleteAllByBusinessUnitId(projectDTO.id());

            //TODO: think about this more (prob will need a rework)
            //Delete all roles in the project
            roleRepository.deleteAllByBusinessUnitId(projectDTO.id());

            //Delete userBURoles
            userBURoleRepository.deleteAllByBusinessUnitId(projectDTO.id());

            //Here's a good idea to call children but they are taking only 1 dto. I'll just make a helper method.
            deleteAllTeams(businessUnitRepository.findAllByCompanyId(projectDTO.id()));

            //Finally delete project
            businessUnitRepository.deleteById(projectDTO.id());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<UserNoPassBusinessUnitRoleDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            List<UserNoPassBusinessUnitRoleDTO> userBURoles = userBURoleMapper.toDTO(
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitProjectId(email, projectDTO.id()));

            if(userBURoles.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBURoles;

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed select! " + e.getMessage());
        }
    }

    @Transactional
    public void createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                throw new UserUnauthenticatedException("User is unauthenticated!");
            }

            UserDTO userDTO = userMapper.toUserDTO(user.get());

            Optional<UserBusinessUnitRole> userBusinessUnitRole = userBURoleRepository.findByUserIdAndBusinessUnitId(userDTO.id(), teamDTO.project().id());

            if(userBusinessUnitRole.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't a part of the parent project!");
            }

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

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void updateTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException {
        createTeam(teamDTO);
    }

    @Transactional
    public void leaveTeam(TeamDTO teamDTO) throws FailedToLeaveException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //Delete all userBURole entries
            List<UserBusinessUnitRole> userBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitId(email, teamDTO.id());

            userBURoleRepository.deleteAll(userBURoles);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToLeaveException("Unsuccessful leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteTeam(TeamDTO teamDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez change as I don't rely on cascading for deletion
            //But isn't worth the extra effort for this project

            //Delete all invites for the team
            inviteRepository.deleteAllByBusinessUnitId(teamDTO.id());

            //TODO: think about this more (prob will need a rework)
            //Delete all roles in the team
            roleRepository.deleteAllByBusinessUnitId(teamDTO.id());

            //Delete userBURoles
            userBURoleRepository.deleteAllByBusinessUnitId(teamDTO.id());

            //Finally delete team
            businessUnitRepository.deleteById(teamDTO.id());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    //Was supposed to be private but transactional requires the method is public
    //Better make sure this is called from somewhere where a transaction already exists rather than risking it an keeping it private
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAllProjects(List<BusinessUnit> projects) throws FailedToDeleteException {
        try{
            //Delete Invites
            projects.forEach(project -> inviteRepository.deleteAllByBusinessUnitId(project.getId()));

            //Delete UBURs
            projects.forEach(project -> userBURoleRepository.deleteAllByBusinessUnitId(project.getId()));

            //Delete Roles
            projects.forEach(project -> roleRepository.deleteAllByBusinessUnitId(project.getId()));

            //Probably an idiotic way to do this
            //Delete child Teams
            List<BusinessUnit> businessUnits = new ArrayList<>();
            projects.forEach(
                    project -> businessUnits.addAll(businessUnitRepository.findAllByProjectId(project.getId())));

            deleteAllTeams(businessUnits);

            //Delete all projects
            businessUnitRepository.deleteAll(projects);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //Was supposed to be private but transactional requires the method is public
    //Better make sure this is called from somewhere where a transaction already exists rather than risking it an keeping it private
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAllTeams(List<BusinessUnit> teams) throws FailedToDeleteException {
        try{
            //Delete Invites
            teams.forEach(team -> inviteRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete UBURs
            teams.forEach(team -> userBURoleRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete Roles
            teams.forEach(team -> roleRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete all teams
            businessUnitRepository.deleteAll(teams);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
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

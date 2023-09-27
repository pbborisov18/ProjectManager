package com.company.projectManager.common.service;


import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.invitation.service.InviteService;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.mapper.RoleMapper;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.mapper.UsersBusinessUnitsRolesMapper;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.repository.BusinessUnitRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.utils.RoleName;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.common.utils.UserBusinessUnitRoleId;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserBusinessUnitRoleService {

    void saveUserBURole(UserWithPassBusinessUnitRoleDTO userBURole) throws FailedToSaveException;

    void deleteUserBURole(UserWithPassBusinessUnitRoleDTO userBuRole) throws FailedToDeleteException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<UserBusinessUnitRoleDTO> findAllCompaniesByUserId(Long userId) throws FailedToSelectException, EntityNotFoundException;

    List<UserBusinessUnitRoleDTO> findCompaniesByAuthenticatedUser() throws UserUnauthenticatedException, FailedToSelectException, EntityNotFoundException;

    void createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToSaveException, FailedToUpdateException, UserNotAuthorizedException;

    void leaveCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, FailedToLeaveException, EntityNotFoundException;

    void deleteCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToDeleteException, FailedToSelectException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<UserBusinessUnitRoleDTO> findAllProjectsByUserIdAndCompany(Long userId, CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException;

    List<UserBusinessUnitRoleDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSelectException, UserNotInBusinessUnitException, EntityNotFoundException;

    void createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException;

    void updateProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException;

    void leaveProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, FailedToLeaveException, EntityNotFoundException;

    void deleteProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSelectException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<UserBusinessUnitRoleDTO> findAllTeamsByUserIdAndProject(Long userId, ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException;

    List<UserBusinessUnitRoleDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSelectException, UserNotInBusinessUnitException, EntityNotFoundException;

    void createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException;

    void updateTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException;

    void leaveTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, FailedToLeaveException, EntityNotFoundException;

    void deleteTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////

    UserBusinessUnitRoleDTO findById(Long userId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException;

    List<UserBusinessUnitRoleDTO> findAllByUserId(Long userId) throws FailedToSelectException, EntityNotFoundException;

    List<UserBusinessUnitRoleDTO> findAllByBusinessUnitId(Long businessUnitId) throws FailedToSelectException, EntityNotFoundException;

    List<UserBusinessUnitRoleDTO> findAllByRoleIdAndBusinessUnitId(Long roleId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException;

    UserBusinessUnitRoleDTO findByUserIdAndBusinessUnitId(Long userId, Long businessUnitId) throws FailedToSelectException, EntityNotFoundException;
}

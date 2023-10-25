package com.company.projectManager.common.service;


import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.exception.*;


import java.util.List;


public interface UserBusinessUnitRoleService {

    List<BusinessUnitAuthoritiesDTO> findAllDistinctCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException;

    void createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateCompany(CompanyDTO companyDTO) throws FailedToUpdateException, EntityNotFoundException;

    void leaveCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, FailedToLeaveException, EntityNotFoundException;

    void deleteCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToDeleteException, FailedToSelectException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<BusinessUnitAuthoritiesDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException;

    void createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException;

    void updateProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException;

    void leaveProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, FailedToLeaveException, EntityNotFoundException;

    void deleteProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSelectException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<BusinessUnitAuthoritiesDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException;

    void createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, FailedToSaveException;

    void updateTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotAuthorizedException, FailedToSaveException, UserNotInBusinessUnitException;

    void leaveTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, UserNotInBusinessUnitException, FailedToDeleteException, FailedToLeaveException, EntityNotFoundException;

    void deleteTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, FailedToDeleteException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException;

    //////////////////////////////////////////////////////////////////////////////////////////
}

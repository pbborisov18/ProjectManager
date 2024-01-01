package com.company.projectManager.common.service;


import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.exception.*;


import java.util.List;

//TODO: Fix naming
public interface UserBusinessUnitRoleService {

    //I'd rather leave all those methods instead of having countless `if` checks inside
    //There will be quite a bit of code duplication but that allows for flexibility in the future
    //And not needing to add more if checks
    //Or needing it to split into more methods in the future (as they are already split)

    List<BusinessUnitAuthoritiesDTO> findAllDistinctCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException;

    void createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateCompany(CompanyDTO companyDTO) throws FailedToUpdateException, EntityNotFoundException;

    void leaveCompany(CompanyDTO companyDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteCompany(CompanyDTO companyDTO) throws FailedToDeleteException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<BusinessUnitAuthoritiesDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException;

    void createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateProject(ProjectDTO projectDTO) throws EntityNotFoundException, FailedToUpdateException;

    void leaveProject(ProjectDTO projectDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteProject(ProjectDTO projectDTO) throws FailedToDeleteException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<BusinessUnitAuthoritiesDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException;

    void createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateTeam(TeamDTO teamDTO) throws EntityNotFoundException, FailedToUpdateException;

    void leaveTeam(TeamDTO teamDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteTeam(TeamDTO teamDTO) throws FailedToDeleteException;

    //////////////////////////////////////////////////////////////////////////////////////////

    void addAuthoritiesToSecurityContext(UserBusinessUnit userBU);
}

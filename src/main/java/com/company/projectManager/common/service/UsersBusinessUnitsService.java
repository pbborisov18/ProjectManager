package com.company.projectManager.common.service;


import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.exception.*;


import java.util.List;

//TODO: I feel like this interface has way too many responsibilities
// and way too many things rely on it. Might need to look into a rework in the future
// Maybe split into company, project, team interfaces?
// And the "random" methods bellow

public interface UsersBusinessUnitsService {

    //I'd rather leave all those methods instead of having countless `if` checks inside
    //There will be quite a bit of code duplication but that allows for flexibility in the future
    //And not needing to add more if checks
    //Or needing it to split into more methods in the future (cuz they are already split)

    List<BusinessUnitAuthoritiesDTO> findAllDistinctCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException;

    BusinessUnitAuthoritiesDTO createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateCompany(CompanyDTO companyDTO) throws FailedToUpdateException, EntityNotFoundException;

    void leaveCompany(CompanyDTO companyDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteCompany(CompanyDTO companyDTO) throws FailedToDeleteException;

    //////////////////////////////////////////////////////////////////////////////////////////

    List<BusinessUnitAuthoritiesDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException;

    BusinessUnitAuthoritiesDTO createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSaveException;

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

    List<UserNoPassDTO> findTheLast30UsersWhoJoinedBU(Long businessUnitId) throws FailedToSelectException;

    UserNoPassBusinessUnitRoleDTO findUserRoles(Long businessUnitId, String email) throws FailedToSelectException, EntityNotFoundException;

    void saveUserRoles(UserNoPassBusinessUnitRoleDTO ubuDTO) throws FailedToSaveException, EntityNotFoundException;

    void kickFromBU(String email, Long businessUnitId) throws EntityNotFoundException, FailedToDeleteException;
}

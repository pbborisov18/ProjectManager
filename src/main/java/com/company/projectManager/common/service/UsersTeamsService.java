package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.exception.*;

import java.util.List;

public interface UsersTeamsService {

    List<BusinessUnitAuthoritiesDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException;

    BusinessUnitAuthoritiesDTO createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateTeam(TeamDTO teamDTO) throws EntityNotFoundException, FailedToUpdateException;

    void leaveTeam(TeamDTO teamDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteTeam(TeamDTO teamDTO) throws FailedToDeleteException;

}

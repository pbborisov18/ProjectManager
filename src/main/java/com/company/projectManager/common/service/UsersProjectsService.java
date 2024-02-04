package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.exception.*;

import java.util.List;

public interface UsersProjectsService {

    List<BusinessUnitAuthoritiesDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException;

    BusinessUnitAuthoritiesDTO createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateProject(ProjectDTO projectDTO) throws EntityNotFoundException, FailedToUpdateException;

    void leaveProject(ProjectDTO projectDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteProject(ProjectDTO projectDTO) throws FailedToDeleteException;

}

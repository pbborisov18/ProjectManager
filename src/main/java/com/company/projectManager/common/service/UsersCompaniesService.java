package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.exception.*;

import java.util.List;

public interface UsersCompaniesService {

    List<BusinessUnitAuthoritiesDTO> findAllDistinctCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException;

    BusinessUnitAuthoritiesDTO createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException;

    void updateCompany(CompanyDTO companyDTO) throws FailedToUpdateException, EntityNotFoundException;

    void leaveCompany(CompanyDTO companyDTO) throws FailedToLeaveException, FailedToDeleteException;

    void deleteCompany(CompanyDTO companyDTO) throws FailedToDeleteException;

}

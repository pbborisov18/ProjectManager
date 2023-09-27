package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.CompanyDTO;
import com.company.projectManager.common.dto.ProjectDTO;
import com.company.projectManager.common.exception.FailedToDeleteException;
import com.company.projectManager.common.exception.FailedToSaveException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.exception.FailedToUpdateException;
import com.company.projectManager.common.exception.EntityNotFoundException;
import com.company.projectManager.common.utils.TypeName;

import java.util.*;


public interface BusinessUnitService {

    void saveBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSaveException;

    void updateBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException, EntityNotFoundException;

    BusinessUnitDTO findBusinessUnitById(Long id) throws FailedToSelectException, EntityNotFoundException;

    List<BusinessUnitDTO> findBusinessUnitsByType(TypeName typeName) throws FailedToSelectException, EntityNotFoundException;

    List<BusinessUnitDTO> findBusinessUnitsByCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException;

    List<BusinessUnitDTO> findBusinessUnitsByCompanyAndType(CompanyDTO companyDTO, TypeName typeName) throws FailedToSelectException, EntityNotFoundException;

    List<BusinessUnitDTO> findBusinessUnitsByProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException;

}

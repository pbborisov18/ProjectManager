package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;

import java.util.List;

public interface RoleService {

    List<RoleDTO> findRolesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException;

    RoleDTO saveRole(RoleDTO role) throws FailedToSaveException, InvalidRoleRequest;

    RoleDTO updateRole(RoleDTO role) throws FailedToUpdateException, EntityNotFoundException;

    void deleteRole(RoleDTO role) throws FailedToDeleteException, EntityNotFoundException, InvalidRoleRequest;

}
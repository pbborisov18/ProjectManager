package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.exception.FailedToDeleteException;
import com.company.projectManager.common.exception.FailedToSaveException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.exception.FailedToUpdateException;
import com.company.projectManager.common.utils.RoleName;
import com.company.projectManager.common.exception.EntityNotFoundException;

import java.util.List;


public interface RoleService {

    void saveRole(RoleDTO roleDTO) throws FailedToSaveException;

    void updateRole(RoleDTO roleDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteRole(RoleDTO roleDTO) throws FailedToDeleteException, EntityNotFoundException;

    RoleDTO findRoleById(Long id) throws FailedToSelectException, EntityNotFoundException;

    RoleDTO findRoleByName(RoleName name) throws FailedToSelectException, EntityNotFoundException;

    List<RoleDTO> findAllRoles() throws FailedToSelectException, EntityNotFoundException;

    List<RoleDTO> findAllRolesById(List<Long> ids) throws FailedToSelectException, EntityNotFoundException;
}

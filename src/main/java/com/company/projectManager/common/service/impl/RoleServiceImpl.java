package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.entity.Role;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.RoleMapper;
import com.company.projectManager.common.repository.RoleRepository;
import com.company.projectManager.common.service.RoleService;
import com.company.projectManager.common.utils.RoleName;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    //Probably won't be used
    public void saveRole(RoleDTO roleDTO) throws FailedToSaveException {
        try {
            Role role = roleMapper.toEntity(roleDTO);

            roleRepository.save(role);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save!" + e.getMessage());
        }
    }

    //Probably won't be used
    public void updateRole(RoleDTO roleDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<Role> existingRole = roleRepository.findById(roleDTO.id());

            if(existingRole.isEmpty()) {
                throw new EntityNotFoundException("Role doesn't exist!");
            }

            Role role = roleMapper.toEntity(roleDTO);

            roleRepository.save(role);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update!" + e.getMessage());
        }
    }

    //Probably will be used in the future in case
    public void deleteRole(RoleDTO roleDTO) throws FailedToDeleteException, EntityNotFoundException {
        try{
            Optional<Role> role = roleRepository.findById(roleDTO.id());

            if(role.isEmpty()) {
                throw new EntityNotFoundException("Role was not found");
            }

            roleRepository.delete(role.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete!" + e.getMessage());
        }
    }

    public RoleDTO findRoleById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Role> role = roleRepository.findById(id);

            if(role.isEmpty()) {
                throw new EntityNotFoundException("Role was not found");
            }

            return roleMapper.toDTO(role.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    public RoleDTO findRoleByName(RoleName name) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<Role> role = roleRepository.findByName(name);

            if(role.isEmpty()) {
                throw new EntityNotFoundException("Role was not found");
            }

            return roleMapper.toDTO(role.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    public List<RoleDTO> findAllRoles() throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Role> roles = (List<Role>) roleRepository.findAll();

            if(roles.size() == 0) {
                throw new EntityNotFoundException("Role was not found");
            }

            return roleMapper.toDTO(roles);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }


}

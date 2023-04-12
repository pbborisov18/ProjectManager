package com.company.projectManager.services;

import com.company.projectManager.DTOs.RoleDTO;
import com.company.projectManager.exceptions.FailedToDeleteException;
import com.company.projectManager.exceptions.FailedToSaveException;
import com.company.projectManager.exceptions.FailedToSelectException;
import com.company.projectManager.exceptions.FailedToUpdateException;
import com.company.projectManager.mappers.RoleMapper;
import com.company.projectManager.models.Role;
import com.company.projectManager.repositories.RoleRepository;
import com.company.projectManager.utils.RoleName;
import com.company.projectManager.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Useless for now but open to extension in the future
@Service
public abstract class  RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleMapper roleMapper;

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

    //Probably won't be used
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

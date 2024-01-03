package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.entity.Role;
import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.RoleMapper;
import com.company.projectManager.common.repository.RoleRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRepository;
import com.company.projectManager.common.service.RoleService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    private final UsersBusinessUnitsRepository usersBURepository;

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(UsersBusinessUnitsRepository usersBURepository, RoleRepository roleRepository, RoleMapper roleMapper) {
        this.usersBURepository = usersBURepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public List<RoleDTO> findRolesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException {
        try{
            List<Role> roles = roleRepository.findAllByBusinessUnitId(businessUnitDTO.id());

            //There should always be at least 1 role
            //so no need to check if this is empty

            return roleMapper.toDTO(roles);

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select!" + e.getMessage());
        }
    }

    public RoleDTO saveRole(RoleDTO roleDTO) throws FailedToSaveException, InvalidRoleRequest {
        try {
            if(roleRepository.countAllByBusinessUnitId(roleDTO.businessUnit().id()) > 50){
                throw new InvalidRoleRequest("Too many roles!");
            }

            Role role = roleRepository.save(roleMapper.toEntity(roleDTO));
            return roleMapper.toDTO(role);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to save!" + e.getMessage());
        }
    }

    //As much as I want to just do .save() (or call saveRole())
    //I can't. I have to make sure that nobody is playing funny here
    //Performance will be worse but it has to happen
    public void updateRole(RoleDTO role) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<Role> foundRole = roleRepository.findById(role.id());

            if(foundRole.isEmpty()){
                throw new EntityNotFoundException("Role doesn't exist");
            }

            //Default and Admin won't be allowed to have name changes
            if(!foundRole.get().getName().equals("Default") || !foundRole.get().getName().equals("Admin")) {
                foundRole.get().setName(role.name());
            }

            foundRole.get().setAuthorities(role.authorities());
            //I am not setting the business unit in case someone gets any funny ideas
            //Id is already the same

            roleRepository.save(foundRole.get());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteRole(RoleDTO role) throws FailedToDeleteException, EntityNotFoundException, InvalidRoleRequest {
        try{
            Optional<Role> foundRole = roleRepository.findById(role.id());

            if(foundRole.isEmpty()) {
                throw new EntityNotFoundException("Role was not found");
            } else if(foundRole.get().getName().equals("Default") || foundRole.get().getName().equals("Admin")) {
                throw new InvalidRoleRequest("These roles aren't allowed to be deleted");
            }

            //Need to check and re-assign roles for all users who have them
            //If the user has no roles left - give him default
            List<UserBusinessUnit> uburs = usersBURepository.findAllByBusinessUnitIdAndRolesId(role.businessUnit().id(), role.id());

            //if it's empty means no one has this role
            //so no need to do checks and assign new roles
            if(!uburs.isEmpty()){

                for (UserBusinessUnit current : uburs) {
                    //if current user has 1 role in the current bu
                    //that means the user has the role we are trying to delete and it's his last one
                    //so give him the default role of the current bu

                    if (current.getRoles().size() == 1L) {
                        usersBURepository.save(
                                new UserBusinessUnit(
                                        null, current.getUser(), current.getBusinessUnit(),
                                        List.of(
                                                roleRepository.findByNameAndBusinessUnitId("Default",
                                                        //This role should always exist for a bu so we can straight up do .get()
                                                        current.getBusinessUnit().getId()).get())));
                    }
                    //Probably inefficient
                    current.getRoles().remove(foundRole.get());
                }

                //Then we just delete the roles after we've made sure no user is left without a role
                //by using save (remember we removed stuff inside the roles list inside)
                //(If we had used delete we would just delete the ubur itself which is not what we want)
                usersBURepository.saveAll(uburs);
            }

            roleRepository.delete(foundRole.get());

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToDeleteException("Failed to delete!" + e.getMessage());
        }
    }

}

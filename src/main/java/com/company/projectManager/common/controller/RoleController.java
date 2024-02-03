package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping({"/roles"})
    @PreAuthorize("authorityCheck(#businessUnitDTO.id(), \"SeePermissions\")")
    public ResponseEntity<Object> getAllRolesOfBusinessUnit(@RequestBody BusinessUnitDTO businessUnitDTO) {
        try {
            List<RoleDTO> roles = roleService.findRolesByBusinessUnit(businessUnitDTO);

            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/createRole"})
    @PreAuthorize("authorityCheck(#role.businessUnit().id(), \"ChangePermissions\")") //Hopefully nobody sends an empty list...
    public ResponseEntity<Object> createPermissions(@RequestBody RoleDTO role){
        try {
            RoleDTO roleDTO = roleService.saveRole(role);

            return new ResponseEntity<>(roleDTO, HttpStatus.OK);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidRoleRequest e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping({"/updateRole"})
    @PreAuthorize("authorityCheck(#role.businessUnit().id(), \"ChangePermissions\")") //Hopefully nobody sends an empty list...
    public ResponseEntity<Object> updatePermissions(@RequestBody RoleDTO role){
        try {
            RoleDTO roleDTO = roleService.updateRole(role);

            return new ResponseEntity<>(roleDTO, HttpStatus.OK);
        } catch (FailedToUpdateException | EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"/deleteRole"})
    @PreAuthorize("authorityCheck(#role.businessUnit().id(), \"ChangePermissions\")") //Hopefully nobody sends an empty list...
    public ResponseEntity<Object> deletePermissions(@RequestBody RoleDTO role){
        try {
            roleService.deleteRole(role);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException | InvalidRoleRequest | EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>("Error: WTF??? Default role doesn't exist somehow...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
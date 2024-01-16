package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.dto.UserNoPassBusinessUnitDTO;
import com.company.projectManager.common.dto.UserNoPassBusinessUnitRoleDTO;
import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.exception.EntityNotFoundException;
import com.company.projectManager.common.exception.FailedToSaveException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.service.RoleService;
import com.company.projectManager.common.service.UsersBusinessUnitsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BusinessUnitController {

    private final UsersBusinessUnitsService usersBusinessUnitsService;

    private final RoleService roleService;

    public BusinessUnitController(UsersBusinessUnitsService usersBusinessUnitsService, RoleService roleService) {
        this.usersBusinessUnitsService = usersBusinessUnitsService;
        this.roleService = roleService;
    }

    @PostMapping("/bu/getLast30Users")
    @PreAuthorize("authorityCheck(#businessUnit.id(), \"SeePermissions\")")
    public ResponseEntity<Object> getLast30UsersWhoJoinedBU(@RequestBody BusinessUnitDTO businessUnit){
        try {
            List<UserNoPassDTO> users = usersBusinessUnitsService.findTheLast30UsersWhoJoinedBU(businessUnit.id());

            if(users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bu/getUserRoles")
    @PreAuthorize("authorityCheck(#ubuDTO.businessUnit().id(), \"SeePermissions\")")
    public ResponseEntity<Object> getUserRoles(@RequestBody UserNoPassBusinessUnitDTO ubuDTO){
        try {
            UserNoPassBusinessUnitRoleDTO ubu = usersBusinessUnitsService.findUserRoles(ubuDTO.businessUnit().id(), ubuDTO.user().email());

            List<RoleDTO> roles = roleService.findRolesByBusinessUnit(ubuDTO.businessUnit());

            return new ResponseEntity<>(Map.of("userBusinessUnit",ubu, "roles", roles), HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bu/changeUserRoles")
    @PreAuthorize("authorityCheck(#ubuDTO.businessUnit().id(), \"ChangePermissions\")")
    public ResponseEntity<Object> changeUserRoles(@RequestBody UserNoPassBusinessUnitRoleDTO ubuDTO) {
        try {
            usersBusinessUnitsService.saveUserRoles(ubuDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping("/bu/kickUser")
    @PreAuthorize("authorityCheck(#ubuDTO.businessUnit().id(), \"ChangePermissions\")")
    public ResponseEntity<Object> kickUser(@RequestBody UserNoPassBusinessUnitDTO ubuDTO) {
        try {
            usersBusinessUnitsService.kickFromBU(ubuDTO.user().email(), ubuDTO.businessUnit().id());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

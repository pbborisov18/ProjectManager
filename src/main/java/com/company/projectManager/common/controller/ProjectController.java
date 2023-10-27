package com.company.projectManager.common.controller;


import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    private final UserBusinessUnitRoleService userBusinessUnitRoleService;

    public ProjectController(UserBusinessUnitRoleService userBusinessUnitRoleService) {
        this.userBusinessUnitRoleService = userBusinessUnitRoleService;
    }

    @PostMapping("/company/projects")
    @PreAuthorize("partOfBU(#companyDTO.id())")
    public ResponseEntity<Object> getAllProjectsOfCompany(@RequestBody CompanyDTO companyDTO){
        try {
            List<BusinessUnitAuthoritiesDTO> userBusinessUnitRoleDTOs =
                    userBusinessUnitRoleService.findAllProjectsByAuthenticatedUserAndCompany(companyDTO);

            if(userBusinessUnitRoleDTOs.isEmpty()){
                return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.OK);

        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/company/createProject")
    @PreAuthorize("authorityCheck(#projectDTO.company().id(), \"CreateChildren\")")
    public ResponseEntity<Object> createProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.createProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserUnauthenticatedException e) { //Pretty much useless check as it should never happen
            //If it is triggerred I guess the security is down. very bad...
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/company/updateProject")
    @PreAuthorize("authorityCheck(#projectDTO.id(), \"UpdateBU\")")
    public ResponseEntity<Object> updateProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.updateProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/company/leaveProject")
    @PreAuthorize("partOfBU(#projectDTO.id())")
    public ResponseEntity<Object> leaveProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.leaveProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException | FailedToLeaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/company/deleteProject")
    @PreAuthorize("authorityCheck(#projectDTO.id(), \"DeleteBU\")")
    public ResponseEntity<Object> deleteProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.deleteProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

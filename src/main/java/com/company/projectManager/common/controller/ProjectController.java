package com.company.projectManager.common.controller;


import com.company.projectManager.common.dto.CompanyDTO;
import com.company.projectManager.common.dto.ProjectDTO;
import com.company.projectManager.common.dto.UserNoPassBusinessUnitRoleDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    private final UserBusinessUnitRoleService userBusinessUnitRoleService;

    public ProjectController(UserBusinessUnitRoleService userBusinessUnitRoleService) {
        this.userBusinessUnitRoleService = userBusinessUnitRoleService;
    }

    //Tested
    @PostMapping("/company/projects")
    public ResponseEntity<Object> getAllProjectsOfCompany(@RequestBody CompanyDTO companyDTO){
        try {
            List<UserNoPassBusinessUnitRoleDTO> userBusinessUnitRoleDTOs = userBusinessUnitRoleService.findAllProjectsByAuthenticatedUserAndCompany(companyDTO);

            if(userBusinessUnitRoleDTOs.isEmpty()){
                return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.OK);

        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //Tested
    @PostMapping("/company/createProject")
    public ResponseEntity<Object> createProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.createProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Tested
    @PutMapping("/company/updateProject")
    public ResponseEntity<Object> updateProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.updateProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotAuthorizedException | UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Tested
    @PutMapping("/company/leaveProject")
    public ResponseEntity<Object> leaveProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.leaveProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToDeleteException | FailedToLeaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //Tested
    @DeleteMapping("/company/deleteProject")
    public ResponseEntity<Object> deleteProject(@RequestBody ProjectDTO projectDTO){
        try {
            userBusinessUnitRoleService.deleteProject(projectDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToDeleteException | FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


}

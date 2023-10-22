package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.ProjectDTO;
import com.company.projectManager.common.dto.TeamDTO;
import com.company.projectManager.common.dto.UserNoPassBusinessUnitRoleDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {

    private final UserBusinessUnitRoleService userBusinessUnitRoleService;

    public TeamController(UserBusinessUnitRoleService userBusinessUnitRoleService) {
        this.userBusinessUnitRoleService = userBusinessUnitRoleService;
    }

    @PostMapping("/company/project/teams")
    public ResponseEntity<Object> getAllTeamsOfProject(@RequestBody ProjectDTO projectDTO){
        try {
            List<UserNoPassBusinessUnitRoleDTO> userBusinessUnitRoleDTOs =
                    userBusinessUnitRoleService.findAllTeamsByAuthenticatedUserAndProject(projectDTO);

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

    @PostMapping("/company/project/createTeam")
    public ResponseEntity<Object> createTeam(@RequestBody TeamDTO teamDTO){
        try {
            userBusinessUnitRoleService.createTeam(teamDTO);

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

    @PutMapping("/company/project/updateTeam")
    public ResponseEntity<Object> updateTeam(@RequestBody TeamDTO teamDTO){
        try {
            userBusinessUnitRoleService.updateTeam(teamDTO);

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

    @DeleteMapping("/company/project/leaveTeam")
    public ResponseEntity<Object> leaveTeam(@RequestBody TeamDTO teamDTO){
        try {
            userBusinessUnitRoleService.leaveTeam(teamDTO);

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


    @DeleteMapping("/company/project/deleteTeam")
    public ResponseEntity<Object> deleteTeam(@RequestBody TeamDTO teamDTO){
        try {
            userBusinessUnitRoleService.deleteTeam(teamDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


}

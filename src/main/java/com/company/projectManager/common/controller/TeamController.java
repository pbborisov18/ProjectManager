package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.UsersTeamsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {

    private final UsersTeamsService usersTeamsService;

    public TeamController(UsersTeamsService usersTeamsService) {
        this.usersTeamsService = usersTeamsService;
    }

    @PostMapping("/company/project/teams")
    @PreAuthorize("partOfBU(#projectDTO.id())")
    public ResponseEntity<Object> getAllTeamsOfProject(@RequestBody ProjectDTO projectDTO){
        try {
            List<BusinessUnitAuthoritiesDTO> userBusinessUnitRoleDTOs =
                    usersTeamsService.findAllTeamsByAuthenticatedUserAndProject(projectDTO);

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
    @PreAuthorize("authorityCheck(#teamDTO.project().id(), \"CreateChildren\")")
    public ResponseEntity<Object> createTeam(@RequestBody TeamDTO teamDTO){
        try {
            BusinessUnitAuthoritiesDTO useBusinessUnitAuthoritiesDTO = usersTeamsService.createTeam(teamDTO);

            return new ResponseEntity<>(useBusinessUnitAuthoritiesDTO, HttpStatus.CREATED);
        } catch (UserUnauthenticatedException e) { //Pretty much useless check as it should never happen
            //If it is triggerred I guess the security is down. very bad...
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/company/project/updateTeam")
    @PreAuthorize("authorityCheck(#teamDTO.id(), \"UpdateBU\")")
    public ResponseEntity<Object> updateTeam(@RequestBody TeamDTO teamDTO){
        try {
            usersTeamsService.updateTeam(teamDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/company/project/leaveTeam")
    @PreAuthorize("partOfBU(#teamDTO.id())")
    public ResponseEntity<Object> leaveTeam(@RequestBody TeamDTO teamDTO){
        try {
            usersTeamsService.leaveTeam(teamDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException | FailedToLeaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/company/project/deleteTeam")
    @PreAuthorize("authorityCheck(#teamDTO.id(), \"DeleteBU\")")
    public ResponseEntity<Object> deleteTeam(@RequestBody TeamDTO teamDTO){
        try {
            usersTeamsService.deleteTeam(teamDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

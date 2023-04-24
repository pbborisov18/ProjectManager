package com.company.projectManager.controllers;

import com.company.projectManager.DTOs.*;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.services.InviteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class InviteController {

    @Autowired
    InviteService inviteService;

    @GetMapping("/invites")
    public ResponseEntity<Object> getInvites(){
        try {
            List<InviteDTOWithoutPassword> invites = inviteService.findInvitesByAuthenticatedReceiverId();

            return new ResponseEntity<>(invites, HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/invites")
    public ResponseEntity<Object> actionInvite(@RequestBody InviteDTOWithoutPassword invite){
        try {
            inviteService.updateInviteByAuthenticatedUser(invite);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InvalidInvitationException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToUpdateException | FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/businessUnit/invites")
    public ResponseEntity<Object> getInvitesByBusinessUnit(@RequestBody BusinessUnitDTO businessUnitDTO){
        try {
            List<InviteDTOWithoutPassword> invites = inviteService.findAllInvitesByBusinessUnit(businessUnitDTO);

            return new ResponseEntity<>(invites, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserNotAuthorizedException | UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/company/invite")
    public ResponseEntity<Object> createInviteForCompany(@RequestBody HashMap<String, Object> requestBody/*@RequestBody CompanyDTO companyDTO, @RequestBody UserWithoutPasswordDTO receiver*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String companyJSON = ow.writeValueAsString(requestBody.get("companyDTO"));
            String userJSON = ow.writeValueAsString(requestBody.get("receiver"));

            ObjectMapper objectMapper = new ObjectMapper();

            return createInviteForBusinessUnit(
                    objectMapper.readValue(companyJSON, CompanyDTO.class),
                    objectMapper.readValue(userJSON, UserWithoutPasswordDTO.class));
        } catch (ClassCastException | JsonProcessingException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/company/project/invite")
    public ResponseEntity<Object> createInviteForProject(@RequestBody HashMap<String, Object> requestBody/*@RequestBody ProjectDTO projectDTO, @RequestBody UserWithoutPasswordDTO receiver*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String projectJSON = ow.writeValueAsString(requestBody.get("projectDTO"));
            String userJSON = ow.writeValueAsString(requestBody.get("receiver"));

            ObjectMapper objectMapper = new ObjectMapper();

            return createInviteForBusinessUnit(
                    objectMapper.readValue(projectJSON, ProjectDTO.class),
                    objectMapper.readValue(userJSON, UserWithoutPasswordDTO.class));
        } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
        }
    }

    @PostMapping("/company/project/team/invite")
    public ResponseEntity<Object> createInviteForTeam(@RequestBody HashMap<String, Object> requestBody/*@RequestBody TeamDTO teamDTO, @RequestBody UserWithoutPasswordDTO receiver*/){
            try {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String teamJSON = ow.writeValueAsString(requestBody.get("teamDTO"));
                String userJSON = ow.writeValueAsString(requestBody.get("receiver"));

                ObjectMapper objectMapper = new ObjectMapper();
                return createInviteForBusinessUnit(
                        objectMapper.readValue(teamJSON, TeamDTO.class),
                        objectMapper.readValue(userJSON, UserWithoutPasswordDTO.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }

    public ResponseEntity<Object> createInviteForBusinessUnit(BusinessUnitDTO businessUnitDTO, UserWithoutPasswordDTO receiver){
        try {
            inviteService.createInviteByAuthenticatedUser(businessUnitDTO, receiver);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (InvalidInvitationException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

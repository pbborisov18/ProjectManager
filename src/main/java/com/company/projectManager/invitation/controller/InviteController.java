package com.company.projectManager.invitation.controller;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTONoPass;
import com.company.projectManager.invitation.service.InviteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class InviteController {

    private final InviteService inviteService;

    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @GetMapping("/invites")
    public ResponseEntity<Object> getInvites(@RequestParam @Valid InviteState inviteState){
        try {
            List<InviteDTONoPass> invites = inviteService.findInvitesByAuthenticatedUserAndState(inviteState);

            return new ResponseEntity<>(invites, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO: For rework
    @PutMapping("/invites")
    public ResponseEntity<Object> actionInvite(@RequestBody @Valid InviteDTONoPass invite){
        try {
            switch (invite.state()) {
                case ACCEPTED -> inviteService.acceptInvite(invite);
                case DECLINED -> inviteService.declineInvite(invite);
                case CANCELLED -> inviteService.cancelInvite(invite);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToUpdateException | FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Why is this Post? Cuz "something something" GET requests with bodies is bad cuz semantics(and spring doesn't read it),
    //"something something", expose the data in the url (send a big object through it even though it has a limit of characters yes)
    //I'd consider this another "mistake" as 403 and 401 being backwards. Been wrong for so long it's right now
    @PostMapping("/businessUnit/invites")
    public ResponseEntity<Object> getInvitesByBusinessUnit(@RequestBody @Valid BusinessUnitDTO businessUnitDTO){
        try {
            List<InviteDTONoPass> invites = inviteService.findAllInvitesByBusinessUnit(businessUnitDTO);

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
    public ResponseEntity<Object> createInviteForCompany(@RequestBody HashMap<String, Object> requestBody/*@RequestBody CompanyDTO companyDTO, @RequestBody UserNoPassDTO receiver*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String companyJSON = ow.writeValueAsString(requestBody.get("companyDTO"));
            String userJSON = ow.writeValueAsString(requestBody.get("receiver"));

            ObjectMapper objectMapper = new ObjectMapper();

            return createInviteForBusinessUnit(
                    objectMapper.readValue(companyJSON, CompanyDTO.class),
                    objectMapper.readValue(userJSON, UserNoPassDTO.class));
        } catch (ClassCastException | JsonProcessingException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/company/project/invite")
    public ResponseEntity<Object> createInviteForProject(@RequestBody HashMap<String, Object> requestBody/*@RequestBody ProjectDTO projectDTO, @RequestBody UserNoPassDTO receiver*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String projectJSON = ow.writeValueAsString(requestBody.get("projectDTO"));
            String userJSON = ow.writeValueAsString(requestBody.get("receiver"));

            ObjectMapper objectMapper = new ObjectMapper();

            return createInviteForBusinessUnit(
                    objectMapper.readValue(projectJSON, ProjectDTO.class),
                    objectMapper.readValue(userJSON, UserNoPassDTO.class));
        } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
        }
    }

    @PostMapping("/company/project/team/invite")
    public ResponseEntity<Object> createInviteForTeam(@RequestBody HashMap<String, Object> requestBody/*@RequestBody TeamDTO teamDTO, @RequestBody UserNoPassDTO receiver*/){
            try {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String teamJSON = ow.writeValueAsString(requestBody.get("teamDTO"));
                String userJSON = ow.writeValueAsString(requestBody.get("receiver"));

                ObjectMapper objectMapper = new ObjectMapper();
                return createInviteForBusinessUnit(
                        objectMapper.readValue(teamJSON, TeamDTO.class),
                        objectMapper.readValue(userJSON, UserNoPassDTO.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }

    public ResponseEntity<Object> createInviteForBusinessUnit(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver){
        try {
            inviteService.createInvite(businessUnitDTO, receiver);

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

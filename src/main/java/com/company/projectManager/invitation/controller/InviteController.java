package com.company.projectManager.invitation.controller;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.BusinessUnitUserNoPassDTO;
import com.company.projectManager.invitation.dto.InviteDTONoPass;
import com.company.projectManager.invitation.service.InviteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("authorityCheck(#businessUnitDTO.id(), \"ManageSentInvites\")")
    public ResponseEntity<Object> getInvitesByBusinessUnit(@RequestBody @Valid BusinessUnitDTO businessUnitDTO){
        try {
            List<InviteDTONoPass> invites = inviteService.findAllInvitesByBusinessUnit(businessUnitDTO);

            return new ResponseEntity<>(invites, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping({"/company/invite", "/company/project/invite", "/company/project/team/invite"})
    @PreAuthorize("authorityCheck(#buUserDTO.businessUnitDTO().id(), \"SendInvite\")")
    public ResponseEntity<Object> createInviteForCompany(@RequestBody BusinessUnitUserNoPassDTO buUserDTO){
        try {
            inviteService.createInvite(buUserDTO.businessUnitDTO(), buUserDTO.userNoPassDTO());

            return new ResponseEntity<>(HttpStatus.CREATED);
        }  catch (InvalidInvitationException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToSaveException e) {
            throw new RuntimeException(e);
        }
    }

}

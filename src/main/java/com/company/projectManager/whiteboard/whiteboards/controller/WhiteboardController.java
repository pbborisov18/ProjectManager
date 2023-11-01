package com.company.projectManager.whiteboard.whiteboards.controller;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardBusinessUnitColumnsDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardBusinessUnitDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.service.WhiteboardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
public class WhiteboardController {

    private final WhiteboardService whiteboardService;

    public WhiteboardController(WhiteboardService whiteboardService) {
        this.whiteboardService = whiteboardService;
    }

    //Why is this Post? Cuz something something GET requests with bodies is bad (and spring doesn't read it),
    //something something, expose the data in the url (no)
    //I'm either breaking one standard or the other. This one is worse to break but easier to implement. (You might say I'm a total idiot for doing this and I'd agree with you)
    @PostMapping(value = {"/company/whiteboard","/company/project/whiteboard", "/company/project/team/whiteboard"})
    @PreAuthorize("partOfBU(#businessUnitDTO.id())")
    public ResponseEntity<Object> getWhiteboardOfCompany(@RequestBody @Valid BusinessUnitDTO businessUnitDTO) {
        try {

            WhiteboardDTO whiteboardDTO = whiteboardService.findWhiteboard(businessUnitDTO);

            return new ResponseEntity<>(whiteboardDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/company/createWhiteboard", "/company/project/createWhiteboard", "/company/project/team/createWhiteboard"})
    @PreAuthorize("authorityCheck(#whiteboardBUDTO.businessUnitDTO().id(),\"CreateWhiteboard\")")
    public ResponseEntity<Object> createWhiteboard(@RequestBody @Valid WhiteboardBusinessUnitDTO whiteboardBUDTO){
        try {
            whiteboardService.createWhiteboard(whiteboardBUDTO.whiteboardDTO(), whiteboardBUDTO.businessUnitDTO());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = {"/company/createCustomWhiteboard", "/company/project/createCustomWhiteboard", "/company/project/team/createCustomWhiteboard"})
    @PreAuthorize("authorityCheck(#whiteboardBUColumnsDTO.businessUnitDTO().id(),\"CreateWhiteboard\")")
    public ResponseEntity<Object> createCustomWhiteboard(@RequestBody @Valid WhiteboardBusinessUnitColumnsDTO whiteboardBUColumnsDTO){
        try {
            whiteboardService.createWhiteboard(
                    whiteboardBUColumnsDTO.whiteboardDTO(),
                    whiteboardBUColumnsDTO.businessUnitDTO(),
                    whiteboardBUColumnsDTO.columns());

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(value = {"/company/deleteWhiteboard", "/company/project/deleteWhiteboard", "/company/project/team/deleteWhiteboard"})
    @PreAuthorize("authorityCheck(#whiteboardBUDTO.businessUnitDTO().id(), \"ManageWhiteboard\")")
    public ResponseEntity<Object> deleteWhiteboard(@RequestBody @Valid WhiteboardBusinessUnitDTO whiteboardBUDTO){
        try {
            whiteboardService.deleteWhiteboard(
                    whiteboardBUDTO.whiteboardDTO(), whiteboardBUDTO.businessUnitDTO());

            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }  catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

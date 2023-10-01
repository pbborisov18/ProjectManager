package com.company.projectManager.whiteboard.whiteboards.controller;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.service.WhiteboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;


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
    public ResponseEntity<Object> getWhiteboardOfCompany(@RequestBody @Valid BusinessUnitDTO businessUnitDTO) {
        try {

            WhiteboardDTO whiteboardDTO = whiteboardService.findWhiteboard(businessUnitDTO);

            //if whiteboard is found return it
            return new ResponseEntity<>(whiteboardDTO, HttpStatus.OK);
        } catch (UserUnauthenticatedException | UserNotInBusinessUnitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e){
            //if it isn't send a request back asking to redirect to */createWhiteboard
            return new ResponseEntity<>("Redirect", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = {"/company/createWhiteboard", "/company/project/createWhiteboard", "/company/project/team/createWhiteboard"})
    public ResponseEntity<Object> createWhiteboard(@RequestBody Map<String, Object> requestBody/*Waiting for these 2 objects - WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String whiteboardJSON = ow.writeValueAsString(requestBody.get("whiteboardDTO"));
            String buJSON = ow.writeValueAsString(requestBody.get("businessUnitDTO"));

            ObjectMapper objectMapper = new ObjectMapper();

            whiteboardService.createWhiteboard(
                    objectMapper.readValue(whiteboardJSON, WhiteboardDTO.class),
                    objectMapper.readValue(buJSON, BusinessUnitDTO.class));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException | FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ClassCastException | JsonProcessingException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Change those paths from the above
    @PostMapping(value = {"/company/createCustomWhiteboard", "/company/project/createCustomWhiteboard", "/company/project/team/createCustomWhiteboard"})
    public ResponseEntity<Object> createCustomWhiteboard(@RequestBody Map<String, Object> requestBody/*Waiting for these 3 objects - WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO, List<ColumnDTOs> columns*/){
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String whiteboardJSON = ow.writeValueAsString(requestBody.get("whiteboardDTO"));
            String buJSON = ow.writeValueAsString(requestBody.get("businessUnitDTO"));
            String columnsJSON = ow.writeValueAsString(requestBody.get("columns"));

            ObjectMapper objectMapper = new ObjectMapper();
            //Idk much about how these work, just stackoverflow
            CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, ColumnDTO.class);

            whiteboardService.createWhiteboard(
                    objectMapper.readValue(whiteboardJSON, WhiteboardDTO.class),
                    objectMapper.readValue(buJSON, BusinessUnitDTO.class),
                    objectMapper.readValue(columnsJSON, typeReference));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException | FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ClassCastException | JsonProcessingException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(value = {"/company/deleteWhiteboard", "/company/project/deleteWhiteboard", "/company/project/team/deleteWhiteboard"})
    public ResponseEntity<Object> deleteWhiteboard(@RequestBody Map<String, Object> requestBody/*Waiting for these 2 objects - WhiteboardDTO whiteboardDTO, BusinessUnitDTO businessUnitDTO*/){
        try {
            whiteboardService.deleteWhiteboard((WhiteboardDTO) requestBody.get("whiteboardDTO"), (BusinessUnitDTO) requestBody.get("businessUnitDTO"));

            return new ResponseEntity<>(HttpStatus.OK);
        }  catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSaveException | FailedToSelectException | FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ClassCastException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

package com.company.projectManager.controllers;

import com.company.projectManager.DTOs.CompanyDTO;
import com.company.projectManager.DTOs.UserBusinessUnitRoleDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.services.UserBusinessUnitRoleService;
import com.company.projectManager.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    @Autowired
    UserBusinessUnitRoleService userBusinessUnitRoleService;

    @GetMapping("/companies")
    public ResponseEntity<Object> getAllCompaniesTheUserIsPartOf(){
        try {
            List<UserBusinessUnitRoleDTO> userBusinessUnitRoleDTOs = userBusinessUnitRoleService.findCompaniesByAuthenticatedUser();


            if(userBusinessUnitRoleDTOs.isEmpty()){
                return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.OK);

        } catch (UserUnauthenticatedException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToSelectException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //Tested
    @PostMapping("/createCompany")
    public ResponseEntity<Object> createCompany(@RequestBody CompanyDTO companyDTO){
        try {
            userBusinessUnitRoleService.createCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Tested
    @PutMapping("/updateCompany")
    public ResponseEntity<Object> updateCompany(@RequestBody CompanyDTO companyDTO){
        try {
            userBusinessUnitRoleService.updateCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToSaveException | FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Tested
    @PutMapping("/leaveCompany")
    public ResponseEntity<Object> leaveCompany(@RequestBody CompanyDTO companyDTO){
        try {
            userBusinessUnitRoleService.leaveCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            //(you can't leave something you are already not a part of)
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToDeleteException | FailedToLeaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //Tested
    @DeleteMapping("/deleteCompany")
    public ResponseEntity<Object> deleteCompany(@RequestBody CompanyDTO companyDTO){
        try {
            userBusinessUnitRoleService.deleteCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUnauthenticatedException e) {
            //Returns 401 which means unauthenticated (not logged in)
            //Reason being someone created this 30 yrs ago and stuff changes
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UserNotInBusinessUnitException | UserNotAuthorizedException e) {
            //Returns 403 which means unauthorized (no permission)
            //Reason being someone created this 30 yrs ago and stuff changes
            //(you can't leave something you are already not a part of)
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (FailedToDeleteException | FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }



}

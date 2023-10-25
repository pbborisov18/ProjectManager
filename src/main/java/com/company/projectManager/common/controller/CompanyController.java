package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.CompanyDTO;
import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    private final UserBusinessUnitRoleService userBusinessUnitRoleService;

    public CompanyController(UserBusinessUnitRoleService userBusinessUnitRoleService) {
        this.userBusinessUnitRoleService = userBusinessUnitRoleService;
    }

    @GetMapping("/companies")
    public ResponseEntity<Object> getAllCompaniesTheUserIsPartOf(){
        try {
            List<BusinessUnitAuthoritiesDTO> userBusinessUnitRoleDTOs =
                    userBusinessUnitRoleService.findAllDistinctCompaniesByAuthenticatedUser();

            if(userBusinessUnitRoleDTOs.isEmpty()){
                return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userBusinessUnitRoleDTOs, HttpStatus.OK);

        } catch (FailedToSelectException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //No authorization to check as this should be allowed for any user
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

    @PutMapping("/updateCompany")
    public ResponseEntity<Object> updateCompany(@RequestBody CompanyDTO companyDTO){
        try {
            userBusinessUnitRoleService.updateCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.OK);
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

    @PutMapping("/leaveCompany")
    @PreAuthorize("partOfBU(#companyDTO.id())")
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

    @DeleteMapping("/deleteCompany")
    @PreAuthorize("authorityCheck(#companyDTO.id(), \"DeleteBU\")")
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

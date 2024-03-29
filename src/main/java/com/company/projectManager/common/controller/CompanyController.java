package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.BusinessUnitAuthoritiesDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.service.UsersCompaniesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    private final UsersCompaniesService usersCompaniesService;

    public CompanyController(UsersCompaniesService usersCompaniesService) {
        this.usersCompaniesService = usersCompaniesService;
    }

    @GetMapping("/companies")
    public ResponseEntity<Object> getAllCompaniesTheUserIsPartOf(){
        try {
            List<BusinessUnitAuthoritiesDTO> userBusinessUnitRoleDTOs =
                    usersCompaniesService.findAllDistinctCompaniesByAuthenticatedUser();

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
            BusinessUnitAuthoritiesDTO useBusinessUnitAuthoritiesDTO = usersCompaniesService.createCompany(companyDTO);

            return new ResponseEntity<>(useBusinessUnitAuthoritiesDTO, HttpStatus.CREATED);
        } catch (UserUnauthenticatedException e) { //Pretty much useless check as it should never happen
            //If it is triggerred I guess the security is down. very bad...
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateCompany")
    @PreAuthorize("authorityCheck(#companyDTO.id(), \"UpdateBU\")")
    public ResponseEntity<Object> updateCompany(@RequestBody CompanyDTO companyDTO){
        try {
            usersCompaniesService.updateCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToUpdateException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/leaveCompany")
    @PreAuthorize("partOfBU(#companyDTO.id())")
    public ResponseEntity<Object> leaveCompany(@RequestBody CompanyDTO companyDTO){
        try {
            usersCompaniesService.leaveCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException | FailedToLeaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteCompany")
    @PreAuthorize("authorityCheck(#companyDTO.id(), \"DeleteBU\")")
    public ResponseEntity<Object> deleteCompany(@RequestBody CompanyDTO companyDTO){
        try {
            usersCompaniesService.deleteCompany(companyDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FailedToDeleteException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

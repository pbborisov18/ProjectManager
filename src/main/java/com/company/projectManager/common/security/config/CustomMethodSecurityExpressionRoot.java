package com.company.projectManager.common.security.config;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.dto.UserNoPassBusinessUnitRoleDTO;
import com.company.projectManager.common.entity.Authority;
import com.company.projectManager.common.exception.EntityNotFoundException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.service.UsersBusinessUnitsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UsersBusinessUnitsService usersBusinessUnitsService;
    private HttpServletRequest request;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    //Hitting the db each time is kinda my only solution for now
    public boolean partOfBU(Long buIdToCheck){
        try {
            //This will throw a EntityNotFoundException if no entry is found
            //meaning user isn't part of the BU
            usersBusinessUnitsService.findUserRoles(buIdToCheck, SecurityContextHolder.getContext().getAuthentication().getName());
            return true;
        } catch (FailedToSelectException | EntityNotFoundException e) {
            return false;
        }
    }

    //Hitting the db twice
    //(sike...way to many times cuz hibernate queries are cool. If there's one place where optimization is a good idea it's here)
    //Once to get all the roles of the user in the BU
    //Then for every role, get all the authorities and check if the one we need is present
    public boolean authorityCheck(Long buIdToCheck, String authorityToCheck){
        UserNoPassBusinessUnitRoleDTO uburDTO;

        try {
            //This will throw a EntityNotFoundException if no entry is found
            //meaning user isn't part of the BU
            //Since hibernate is "fancy" (I'm an idiot who hasn't optimized it) it hits the db for the authorities of each role as well
            //so we can skip hitting the db again for the authorities of each role
            uburDTO = usersBusinessUnitsService.findUserRoles(buIdToCheck, SecurityContextHolder.getContext().getAuthentication().getName());

        } catch (FailedToSelectException | EntityNotFoundException e) {
            return false;
        }

        //Loop through each role checking if the authority we need is present
        for(RoleDTO role : uburDTO.roles()){
            for(Authority authority : role.authorities()) {
                if(authority.getName().equals(authorityToCheck)){
                    return true;
                }
            }
        }

        return false;
    }

    public void setUsersBusinessUnitsService(UsersBusinessUnitsService usersBusinessUnitsService){
        this.usersBusinessUnitsService = usersBusinessUnitsService;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }
}

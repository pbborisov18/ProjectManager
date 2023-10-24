package com.company.projectManager.common.security.config;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.entity.Authority;
import com.company.projectManager.common.exception.EntityNotFoundException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private RoleService roleService;
    private HttpServletRequest request;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean partOfBU(Long buIdToCheck){
        for (GrantedAuthority userAuthorities : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
            String[] userAuthoritiesId = userAuthorities.getAuthority().split(":");

            if(userAuthoritiesId[2].equals(String.valueOf(buIdToCheck))){
                return true;
            }
        }

        return false;
    }

    public boolean authorityCheck(Long buIdToCheck, String authorityToCheck){
        List<Long> roleIdsToSearchFor = new ArrayList<>();
        //Loop through every securityId
        for (GrantedAuthority userAuthorities : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
            //"Decode" each securityId
            //0. UserBusinessUnitRole id
            //1. User id
            //2. BusinessUnit id
            //3. Role ids
            String[] userAuthoritiesId = userAuthorities.getAuthority().split(":");

            //if the correct BU is found that means the user is in the BU
            if(userAuthoritiesId[2].equals(String.valueOf(buIdToCheck))){
                //That's all the role ids the user has in that BU
                roleIdsToSearchFor.addAll(Arrays.stream(
                        userAuthoritiesId[3].substring(1, userAuthoritiesId[3].length() - 1).split(", "))
                                .mapToLong(Long::parseLong).boxed().toList());
            }
        }

        //Get all the role objects by Id
        List<RoleDTO> roles = null;
        try {
            roles = roleService.findAllRolesById(roleIdsToSearchFor);
        } catch (FailedToSelectException | EntityNotFoundException e) {
            return false;
        }

        //Loop through each role checking if the authority we need is present
        for(RoleDTO role : roles){
            for(Authority authority : role.authorities()) {
                if(authority.getName().equals(authorityToCheck)){
                    return true;
                }
            }
        }

        return false;
    }

    public void setRoleService(RoleService roleService){
        this.roleService = roleService;
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

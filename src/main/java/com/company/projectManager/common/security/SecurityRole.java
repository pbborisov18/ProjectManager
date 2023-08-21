package com.company.projectManager.common.security;

import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.entity.Role;
import org.springframework.security.core.GrantedAuthority;

public class SecurityRole implements GrantedAuthority {

    private final Role role;

    private final BusinessUnit businessUnit;

    public SecurityRole(Role role, BusinessUnit businessUnit) {
        this.role = role;
        this.businessUnit = businessUnit;
    }


    @Override
    public String getAuthority() {
        return businessUnit.getId().toString() + ":" + role.getName();
    }

    public Role getRole() {
        return role;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }
}

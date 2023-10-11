package com.company.projectManager.common.security;

import org.springframework.security.core.GrantedAuthority;

public class SecurityIds implements GrantedAuthority {

    private Long uBURId;
    private Long userId;
    private Long businessUnitId;
    private Long roleId;

    public SecurityIds(Long uBURId, Long userId, Long businessUnitId, Long roleId) {
        this.uBURId = uBURId;
        this.userId = userId;
        this.businessUnitId = businessUnitId;
        this.roleId = roleId;
    }

    @Override
    public String getAuthority() {
        return this.uBURId + ":" +
                this.userId + ":" +
                this.businessUnitId + ":" +
                this.roleId;
    }

    public Long getuBURId() {
        return uBURId;
    }

    public void setuBURId(Long uBURId) {
        this.uBURId = uBURId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

package com.company.projectManager.common.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class SecurityIds implements GrantedAuthority {

    private Long uBUId;
    private Long userId;
    private Long businessUnitId;
    private List<Long> roleIds;

    public SecurityIds(Long uBURId, Long userId, Long businessUnitId, List<Long> roleIds) {
        this.uBUId = uBURId;
        this.userId = userId;
        this.businessUnitId = businessUnitId;
        this.roleIds = roleIds;
    }

    @Override
    public String getAuthority() {
        return this.uBUId + ":" +
                this.userId + ":" +
                this.businessUnitId + ":" +
                this.roleIds;
    }

    public Long getuBUId() {
        return uBUId;
    }

    public void setuBUId(Long uBUId) {
        this.uBUId = uBUId;
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

    public List<Long> getRoleId() {
        return roleIds;
    }

    public void setRoleId(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}

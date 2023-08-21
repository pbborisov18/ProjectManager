package com.company.projectManager.common.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter @Setter
public class UserBusinessUnitRoleId implements Serializable {

    private Long user;
    private Long businessUnit;

    public UserBusinessUnitRoleId(){}

    public UserBusinessUnitRoleId(Long user, Long businessUnit) {
        this.user = user;
        this.businessUnit = businessUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBusinessUnitRoleId that = (UserBusinessUnitRoleId) o;

        if (!user.equals(that.user)) return false;
        return businessUnit.equals(that.businessUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getBusinessUnit());
    }

}

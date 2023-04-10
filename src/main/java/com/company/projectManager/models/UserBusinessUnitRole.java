package com.company.projectManager.models;

import com.company.projectManager.utils.UserBusinessUnitRoleId;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "UsersBusinessUnitsRoles", uniqueConstraints = {@UniqueConstraint(columnNames = {"UsersId", "BusinessUnitsId"})})
@IdClass(UserBusinessUnitRoleId.class)
public class UserBusinessUnitRole {

    @Id
    @ManyToOne
    @JoinColumn(name = "UsersId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "BusinessUnitsId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private BusinessUnit businessUnit;

    @ManyToOne
    @JoinColumn(name = "RolesId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private Role role;

    public UserBusinessUnitRole(User user, BusinessUnit businessUnit, Role role) {
        this.user = user;
        this.businessUnit = businessUnit;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserBusinessUnitRole that = (UserBusinessUnitRole) o;
        return getUser() != null && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public User getUser() {
        setUser((User) Hibernate.unproxy(this.user));
        return user;
    }

    public BusinessUnit getBusinessUnit() {
        setBusinessUnit((BusinessUnit) Hibernate.unproxy(this.businessUnit));
        return businessUnit;
    }

    public Role getRole() {
        setRole((Role) Hibernate.unproxy(this.role));
        return role;
    }
}

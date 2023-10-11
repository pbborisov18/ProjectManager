package com.company.projectManager.common.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "UsersBusinessUnitsRoles", uniqueConstraints = {@UniqueConstraint(columnNames = {"UsersId", "BusinessUnitsId", "RolesId"})})
public class UserBusinessUnitRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UsersId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BusinessUnitsId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private BusinessUnit businessUnit;

    @ManyToOne
    @JoinColumn(name = "RolesId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private Role role;

    public UserBusinessUnitRole() {
    }

    public UserBusinessUnitRole(Long id, User user, BusinessUnit businessUnit, Role role) {
        this.id = id;
        this.user = user;
        this.businessUnit = businessUnit;
        this.role = role;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserBusinessUnitRole that = (UserBusinessUnitRole) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

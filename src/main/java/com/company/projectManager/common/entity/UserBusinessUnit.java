package com.company.projectManager.common.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users_business_units", schema = "v1", uniqueConstraints = {@UniqueConstraint(columnNames = {"users_id", "business_units_id"})})
public class UserBusinessUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @Cascade(CascadeType.MERGE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "business_units_id", referencedColumnName = "id")
    @Cascade(CascadeType.MERGE)
    private BusinessUnit businessUnit;

    @ManyToMany
    @JoinTable(
            name = "users_business_units_roles", schema = "v1",
            joinColumns = @JoinColumn(name = "users_business_units_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    @Cascade(CascadeType.MERGE)
    private List<Role> roles;

    public UserBusinessUnit() {
    }

    public UserBusinessUnit(Long id, User user, BusinessUnit businessUnit, List<Role> roles) {
        this.id = id;
        this.user = user;
        this.businessUnit = businessUnit;
        this.roles = roles;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserBusinessUnit that = (UserBusinessUnit) o;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}

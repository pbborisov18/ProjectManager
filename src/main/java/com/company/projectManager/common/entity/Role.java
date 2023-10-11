package com.company.projectManager.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.annotations.CascadeType;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "RolesAuthorities",
            joinColumns = @JoinColumn(name = "RolesId"),
            inverseJoinColumns = @JoinColumn(name = "AuthoritiesId"))
    private List<Authority> authorities;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "BusinessUnitsId", referencedColumnName = "Id")
    @Cascade(CascadeType.MERGE)
    private BusinessUnit businessUnit;

    public Role() {
    }

    public Role(Long id, String name, List<Authority> authorities, BusinessUnit businessUnit) {
        this.id = id;
        this.name = name;
        this.authorities = authorities;
        this.businessUnit = businessUnit;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Role role = (Role) o;
        return getId() != null && Objects.equals(getId(), role.getId());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Authority> getAuthorities() {
        setAuthorities((List<Authority>) Hibernate.unproxy(this.authorities));
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

}

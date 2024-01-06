package com.company.projectManager.common.entity;

import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "business_units", schema = "v1")
//Could've used @Inheritance() but I'm basically doing it manually
//And I'm doing a combination of JOINED and SINGLE_TABLE (which could've been made just SINGLE_TABLE but I didn't think of it then)
public class BusinessUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TypeName type;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "companies_id", referencedColumnName = "id")
    private BusinessUnit company;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "projects_id", referencedColumnName = "id")
    private BusinessUnit project;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "whiteboards_id", referencedColumnName = "id")
    @Cascade(CascadeType.MERGE)
    private Whiteboard whiteboard;

    public BusinessUnit() {}

    //TODO: Builder pattern


    public BusinessUnit(Long id, String name, TypeName type, @Nullable BusinessUnit company, @Nullable BusinessUnit project, @Nullable Whiteboard whiteboard) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.company = company;
        this.project = project;
        this.whiteboard = whiteboard;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BusinessUnit that = (BusinessUnit) o;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeName getType() {
        return type;
    }

    public void setType(TypeName type) {
        this.type = type;
    }

    @Nullable
    public BusinessUnit getCompany() {
        return company;
    }

    public void setCompany(@Nullable BusinessUnit company) {
        this.company = company;
    }

    @Nullable
    public BusinessUnit getProject() {
        return project;
    }

    public void setProject(@Nullable BusinessUnit project) {
        this.project = project;
    }

    @Nullable
    public Whiteboard getWhiteboard() {
        return whiteboard;
    }

    public void setWhiteboard(@Nullable Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
    }

}

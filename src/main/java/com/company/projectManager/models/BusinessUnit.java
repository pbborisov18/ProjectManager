package com.company.projectManager.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;


@Entity
@Setter
@NoArgsConstructor
@Table(name = "BusinessUnits")
public class BusinessUnit {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @NotBlank
        private String name;

        @ManyToOne
        @JoinColumn(name = "TypesId", referencedColumnName = "Id")
        @NotNull
        private Type type;

        @Nullable
        @ManyToOne
        @JoinColumn(name = "CompaniesId", referencedColumnName = "Id")
        private BusinessUnit company;

        @Nullable
        @ManyToOne
        @JoinColumn(name = "ProjectsId", referencedColumnName = "Id")
        private BusinessUnit project;

        @Nullable
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "WhiteboardsId", referencedColumnName = "Id")
        private Whiteboard whiteboard;

        public BusinessUnit(Long id, String name, Type type, @Nullable BusinessUnit company, @Nullable BusinessUnit project, @Nullable Whiteboard whiteboard) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.company = company;
            this.project = project;
            this.whiteboard = whiteboard;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
            BusinessUnit that = (BusinessUnit) o;
            return getId() != null && Objects.equals(getId(), that.getId());
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            setType((Type) Hibernate.unproxy(this.type));
            return type;
        }

        @Nullable
        public BusinessUnit getCompany() {
            return company;
        }

        @Nullable
        public BusinessUnit getProject() {
            return project;
        }

        public Whiteboard getWhiteboard() {
            setWhiteboard((Whiteboard) Hibernate.unproxy(this.whiteboard));
            return whiteboard;
        }

}

package com.company.projectManager.common.entity;

import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "BusinessUnits")
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
        private TypeName type;

        @Nullable
        @ManyToOne
        @JoinColumn(name = "CompaniesId", referencedColumnName = "Id")
        private BusinessUnit company;

        @Nullable
        @ManyToOne
        @JoinColumn(name = "ProjectsId", referencedColumnName = "Id")
        private BusinessUnit project;

        @Nullable
        @ManyToOne
        @JoinColumn(name = "WhiteboardsId", referencedColumnName = "Id")
        @Cascade(CascadeType.MERGE)
        private Whiteboard whiteboard;

        public BusinessUnit(Long id, String name, TypeName type, @Nullable BusinessUnit company, @Nullable BusinessUnit project, @Nullable Whiteboard whiteboard) {
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

}

package com.company.projectManager.whiteboard.notes.entity;

import com.company.projectManager.whiteboard.columns.entity.Column;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "Notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ColumnsId")
    @Cascade(CascadeType.MERGE)
    private Column column;


    public Note(Long id, String name, String description, Column column) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Note note = (Note) o;
        return getId() != null && Objects.equals(getId(), note.getId());
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

    public String getDescription() {
        return description;
    }

    public Column getColumn() {
        setColumn((Column) Hibernate.unproxy(this.column));
        return column;
    }
}

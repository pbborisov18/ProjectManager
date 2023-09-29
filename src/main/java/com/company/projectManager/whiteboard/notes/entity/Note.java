package com.company.projectManager.whiteboard.notes.entity;

import com.company.projectManager.whiteboard.columns.entity.Column;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;

import java.util.Objects;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "Notes", uniqueConstraints = {@UniqueConstraint(columnNames = {"ColumnsId", "Position"})})
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
    //If I merge the Note I want the column to be merged too
    //You'd wonder why I can't use PERSIST here together with merge? Well JPA considers persist first and
    //throws an exception before it can try merge (so the transaction is rolled back)
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ColumnsId")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.SAVE_UPDATE}) //This isn't substitute (hibernate documentation)
    private Column column;

    @NotNull
    private Long position;

    public Note(Long id, String name, String description, Column column, Long position) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.column = column;
        this.position = position;
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

    public Long getPosition() {
        return position;
    }
}

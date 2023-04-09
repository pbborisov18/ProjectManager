package com.company.projectManager.models;

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
    @JoinColumn(name="WhiteboardsId", nullable = false)
    private Whiteboard whiteboard;

    public Note(Long id, String name, String description, Whiteboard whiteboard) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.whiteboard = whiteboard;
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

    public Whiteboard getWhiteboard() {
        setWhiteboard((Whiteboard) Hibernate.unproxy(this.whiteboard));
        return whiteboard;
    }
}

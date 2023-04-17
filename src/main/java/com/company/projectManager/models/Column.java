package com.company.projectManager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CascadeType;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.Objects;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "Columns", uniqueConstraints = {@UniqueConstraint(columnNames = {"WhiteboardsId", "Position"})})
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "WhiteboardsId")
    @Cascade(CascadeType.MERGE)
    private Whiteboard whiteboard;


    @NotNull
    private int position;

    public Column(Long id, String name, Whiteboard whiteboard, int position) {
        this.id = id;
        this.name = name;
        this.whiteboard = whiteboard;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Column column = (Column) o;
        return id != null && Objects.equals(id, column.id);
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

    public Whiteboard getWhiteboard() {
        setWhiteboard((Whiteboard) Hibernate.unproxy(this.whiteboard));
        return whiteboard;
    }

    public int getPosition() {
        return position;
    }
}

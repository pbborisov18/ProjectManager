package com.company.projectManager.whiteboard.columns.entity;

import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import jakarta.persistence.CascadeType;
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

    @NotNull
    //If I merge the Note I want the column to be merged too
    //You'd wonder why I can't use PERSIST here together with merge? Well JPA considers persist first and
    //throws an exception before it can try merge (so the transaction is rolled back)
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "WhiteboardsId")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.SAVE_UPDATE}) //This isn't substitute (hibernate documentation)
    private Whiteboard whiteboard;

    @NotNull
    private Long position;

    public Column(Long id, String name, Whiteboard whiteboard, Long position) {
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

    public Long getPosition() {
        return position;
    }
}

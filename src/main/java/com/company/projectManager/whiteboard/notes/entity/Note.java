package com.company.projectManager.whiteboard.notes.entity;

import com.company.projectManager.whiteboard.columns.entity.Column;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "notes", schema = "v1", uniqueConstraints = {@UniqueConstraint(columnNames = {"columns_id", "position"})})
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
    @JoinColumn(name = "columns_id")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE}) //This isn't substitute (hibernate documentation)
    private Column column;

    @NotNull
    private Long position;

    public Note() {
    }

    public Note(Long id, String name, String description, Column column, Long position) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.column = column;
        this.position = position;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Note note = (Note) o;
        return getId() != null && Objects.equals(getId(), note.getId());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

}

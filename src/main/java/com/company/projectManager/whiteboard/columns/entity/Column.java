package com.company.projectManager.whiteboard.columns.entity;

import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.CascadeType;
import org.hibernate.annotations.Cascade;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "columns", schema = "v1", uniqueConstraints = {@UniqueConstraint(columnNames = {"whiteboards_id", "position"})})
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
    @JoinColumn(name = "whiteboards_id")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE}) //This isn't substitute (hibernate documentation)
    private Whiteboard whiteboard;

    //Prob shouldn't be long
    @NotNull
    private Long position;

    public Column() {
    }

    public Column(Long id, String name, Whiteboard whiteboard, Long position) {
        this.id = id;
        this.name = name;
        this.whiteboard = whiteboard;
        this.position = position;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Column column = (Column) o;
        return getId() != null && Objects.equals(getId(), column.getId());
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

    public Whiteboard getWhiteboard() {
        return whiteboard;
    }

    public void setWhiteboard(Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

}

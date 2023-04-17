package com.company.projectManager.models;

import com.company.projectManager.utils.InviteState;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "Invites", uniqueConstraints = {@UniqueConstraint(columnNames = {"ReceiverId", "BusinessUnitsId"})})
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InviteState state;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SenderId")
    private User sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ReceiverId")
    private User receiver;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "BusinessUnitsId")
    @Cascade(CascadeType.MERGE)
    private BusinessUnit businessUnit;

    public Invite(Long id, InviteState state, User sender, User receiver, BusinessUnit businessUnit) {
        this.id = id;
        this.state = state;
        this.sender = sender;
        this.receiver = receiver;
        this.businessUnit = businessUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Invite invite = (Invite) o;
        return getId() != null && Objects.equals(getId(), invite.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

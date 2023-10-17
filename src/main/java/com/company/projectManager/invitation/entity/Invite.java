package com.company.projectManager.invitation.entity;

import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.utils.InviteState;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.util.Objects;

@Entity
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
    @JoinColumn(name = "ReceiverId")
    @Cascade(CascadeType.MERGE)
    private User receiver;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "BusinessUnitsId")
    @Cascade(CascadeType.MERGE)
    private BusinessUnit businessUnit;

    public Invite() {
    }

    public Invite(Long id, InviteState state, User receiver, BusinessUnit businessUnit) {
        this.id = id;
        this.state = state;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InviteState getState() {
        return state;
    }

    public void setState(InviteState state) {
        this.state = state;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }
}

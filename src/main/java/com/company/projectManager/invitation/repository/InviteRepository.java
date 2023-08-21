package com.company.projectManager.invitation.repository;

import com.company.projectManager.invitation.entity.Invite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InviteRepository extends CrudRepository<Invite, Long>{
    @Query("SELECT i " +
            "FROM Invite i " +
            "WHERE i.receiver.id = :receiverId AND i.state = 'PENDING'")
    List<Invite> findByReceiverIdAndStateNotIn(Long receiverId);

    List<Invite> findAllByBusinessUnitId(Long businessUnitId);
}

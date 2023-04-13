package com.company.projectManager.repositories;

import com.company.projectManager.models.Invite;
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

package com.company.projectManager.invitation.repository;

import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.entity.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends CrudRepository<Invite, Long>{

    Optional<Invite> findInviteByBusinessUnit_IdAndReceiver_Email(Long id, String email);

    List<Invite> findByReceiver_EmailAndState(String email, InviteState state);

    List<Invite> findAllByBusinessUnitId(Long businessUnitId);
}

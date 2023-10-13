package com.company.projectManager.invitation.repository;

import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.entity.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InviteRepository extends CrudRepository<Invite, Long>{

    List<Invite> findByReceiver_EmailAndState(String email, InviteState state);

    List<Invite> findAllByBusinessUnitId(Long businessUnitId);
}

package com.company.projectManager.invitation.repository;

import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.entity.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends CrudRepository<Invite, Long>{

    Optional<Invite> findInviteByBusinessUnitIdAndReceiverEmail(Long id, String email);

    List<Invite> findByReceiverEmailAndState(String email, InviteState state);

    List<Invite> findAllByBusinessUnitId(Long businessUnitId);

    void deleteAllByBusinessUnitId(Long businessUnitId);

    void deleteAllByReceiverEmailAndBusinessUnitCompanyId(String email, Long businessUnitId);

    void deleteAllByReceiverEmailAndBusinessUnitProjectId(String email, Long businessUnitId);

    void deleteByReceiverEmailAndBusinessUnitId(String email, Long businessUnitId);

}

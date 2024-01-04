package com.company.projectManager.invitation.repository;

import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.entity.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends CrudRepository<Invite, Long>{

    Optional<Invite> findByReceiverEmailIgnoreCaseAndBusinessUnitId(String email, Long id);

    List<Invite> findByReceiverEmailIgnoreCaseAndState(String email, InviteState state);

    List<Invite> findAllByBusinessUnitId(Long businessUnitId);

    void deleteAllByBusinessUnitId(Long businessUnitId);

    void deleteAllByReceiverEmailIgnoreCaseAndBusinessUnitCompanyId(String email, Long businessUnitId);

    void deleteAllByReceiverEmailIgnoreCaseAndBusinessUnitProjectId(String email, Long businessUnitId);

    void deleteByReceiverEmailIgnoreCaseAndBusinessUnitId(String email, Long businessUnitId);

}

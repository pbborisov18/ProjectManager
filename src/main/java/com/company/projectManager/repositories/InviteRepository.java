package com.company.projectManager.repositories;

import com.company.projectManager.models.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InviteRepository extends CrudRepository<Invite, Long>{
    List<Invite> findByReceiverId(Long receiverId);
}

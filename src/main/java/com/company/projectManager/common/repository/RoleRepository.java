package com.company.projectManager.common.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.common.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

    List<Role> findAllByBusinessUnitId(Long id);

    void deleteAllByBusinessUnitId(Long id);
}

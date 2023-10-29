package com.company.projectManager.common.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.common.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    List<Role> findAllByBusinessUnitId(Long id);

    Optional<Role> findByNameAndBusinessUnitId(String name, Long id);

    Long countAllByBusinessUnitId(Long id);

    void deleteAllByBusinessUnitId(Long id);
}

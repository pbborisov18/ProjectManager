package com.company.projectManager.common.repository;

import com.company.projectManager.common.entity.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthoritityRepository extends CrudRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}

package com.company.projectManager.common.repository;

import com.company.projectManager.common.entity.Authority;
import com.company.projectManager.common.utils.ReadOnlyRepository;

import java.util.Optional;

public interface AuthoritityRepository extends ReadOnlyRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}

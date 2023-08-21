package com.company.projectManager.common.repository;

import com.company.projectManager.common.utils.TypeName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.common.entity.Type;

import java.util.Optional;

@Repository
public interface TypeRepository extends CrudRepository<Type, Long> {
    Optional<Type> findByName(TypeName name);

}

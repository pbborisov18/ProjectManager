package com.company.projectManager.repositories;

import com.company.projectManager.utils.TypeName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.models.Type;

import java.util.Optional;

@Repository
public interface TypeRepository extends CrudRepository<Type, Long> {
    Optional<Type> findByName(TypeName name);

}

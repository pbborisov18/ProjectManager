package com.company.projectManager.common.repository;

import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.utils.TypeName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersBusinessUnitsRolesRepository extends CrudRepository<UserBusinessUnitRole, Long> {

    List<UserBusinessUnitRole> findAllByUserId(Long userId);

    List<UserBusinessUnitRole> findAllByUserEmailAndBusinessUnitType(String email, TypeName typeName);

    List<UserBusinessUnitRole> findAllByUserEmailAndBusinessUnitCompanyIdAndBusinessUnitType(String email, Long companyId, TypeName type);

    List<UserBusinessUnitRole> findAllByUserEmailAndBusinessUnitProjectId(String email, Long projectId);

    List<UserBusinessUnitRole> findAllByUserEmailAndBusinessUnitCompanyId(String email, Long companyId);

    List<UserBusinessUnitRole> findAllByBusinessUnitId(Long businessUnitId);

    Optional<UserBusinessUnitRole> findByUserIdAndBusinessUnitId(Long userId, Long businessUnitId);

    List<UserBusinessUnitRole> findAllByUserEmailAndBusinessUnitId(String email, Long businessUnitId);

    Optional<UserBusinessUnitRole> findByUserIdAndBusinessUnitWhiteboardId(Long userId, Long whiteboardId);

    void deleteAllByBusinessUnitId(Long businessUnitId);
}

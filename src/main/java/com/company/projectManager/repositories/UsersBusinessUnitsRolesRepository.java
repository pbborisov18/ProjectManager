package com.company.projectManager.repositories;

import com.company.projectManager.models.UserBusinessUnitRole;
import com.company.projectManager.utils.TypeName;
import com.company.projectManager.utils.UserBusinessUnitRoleId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersBusinessUnitsRolesRepository extends CrudRepository<UserBusinessUnitRole, UserBusinessUnitRoleId> {

    List<UserBusinessUnitRole> findAllByUserId(Long userId);

    List<UserBusinessUnitRole> findAllByUserIdAndBusinessUnitTypeName(Long userId, TypeName typeName);

    List<UserBusinessUnitRole> findAllByUserIdAndBusinessUnitTypeNameAndBusinessUnitCompanyId(Long userId, TypeName typeName, Long companyId);

    List<UserBusinessUnitRole> findAllByUserIdAndBusinessUnitTypeNameAndBusinessUnitProjectId(Long userId, TypeName typeName, Long projectId);

    List<UserBusinessUnitRole> findAllByUserIdAndBusinessUnitCompanyId(Long userId, Long companyId);

    List<UserBusinessUnitRole> findAllByUserIdAndBusinessUnitProjectId(Long userId, Long projectId);

    List<UserBusinessUnitRole> findAllByBusinessUnitId(Long businessUnitId);

    List<UserBusinessUnitRole> findAllByRoleIdAndBusinessUnitId(Long roleId, Long businessUnitId);

    Optional<UserBusinessUnitRole> findByUserIdAndBusinessUnitId(Long userId, Long businessUnitId);

    Optional<UserBusinessUnitRole> findByUserIdAndBusinessUnitWhiteboardId(Long userId, Long whiteboardId);
}

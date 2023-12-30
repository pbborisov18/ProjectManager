package com.company.projectManager.common.repository;

import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.utils.TypeName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
//TODO: To rename to UsersBusinessUnitsRepository
public interface UsersBusinessUnitsRolesRepository extends CrudRepository<UserBusinessUnit, Long> {

    List<UserBusinessUnit> findAllByUserId(Long userId);

    List<UserBusinessUnit> findAllByUserEmailAndBusinessUnitType(String email, TypeName typeName);

    List<UserBusinessUnit> findAllByUserEmailAndBusinessUnitCompanyIdAndBusinessUnitType(String email, Long companyId, TypeName type);

    List<UserBusinessUnit> findAllByUserEmailAndBusinessUnitProjectId(String email, Long projectId);

    List<UserBusinessUnit> findAllByUserEmailAndBusinessUnitCompanyId(String email, Long companyId);

    List<UserBusinessUnit> findAllByUserEmailAndBusinessUnitId(String email, Long businessUnitId);

    List<UserBusinessUnit> findAllByBusinessUnitIdAndRolesId(Long businessUnitId, Long roleId);

    Long countAllByBusinessUnitId(Long businessUnitId);

    void deleteAllByBusinessUnitId(Long businessUnitId);
}

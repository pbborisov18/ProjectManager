package com.company.projectManager.common.repository;

import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.utils.TypeName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersBusinessUnitsRepository extends CrudRepository<UserBusinessUnit, Long> {

    List<UserBusinessUnit> findAllByUserEmailIgnoreCaseAndBusinessUnitType(String email, TypeName typeName);

    List<UserBusinessUnit> findAllByUserEmailIgnoreCaseAndBusinessUnitCompanyIdAndBusinessUnitType(String email, Long companyId, TypeName type);

    List<UserBusinessUnit> findAllByUserEmailIgnoreCaseAndBusinessUnitProjectId(String email, Long projectId);

    List<UserBusinessUnit> findAllByUserEmailIgnoreCaseAndBusinessUnitCompanyId(String email, Long companyId);

    Optional<UserBusinessUnit> findByUserEmailIgnoreCaseAndBusinessUnitId(String email, Long businessUnitId);

    List<UserBusinessUnit> findAllByBusinessUnitIdAndRolesId(Long businessUnitId, Long roleId);

    List<UserBusinessUnit> findTop30ByBusinessUnitIdOrderByIdDesc(Long businessUnitId);

    Long countAllByBusinessUnitId(Long businessUnitId);

    void deleteAllByBusinessUnitId(Long businessUnitId);

}

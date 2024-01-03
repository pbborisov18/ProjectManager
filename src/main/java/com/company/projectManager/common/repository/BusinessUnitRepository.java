package com.company.projectManager.common.repository;

import com.company.projectManager.common.utils.TypeName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.common.entity.BusinessUnit;

import java.util.List;

@Repository
public interface BusinessUnitRepository extends CrudRepository<BusinessUnit, Long> {

    List<BusinessUnit> findAllByCompanyId(Long id);

    List<BusinessUnit> findAllByCompanyIdAndType(Long companyId, TypeName typeName);

    List<BusinessUnit> findAllByProjectId(Long id);
}

package com.company.projectManager.common.repository;

import com.company.projectManager.common.utils.TypeName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.common.entity.BusinessUnit;

import java.util.List;

@Repository
public interface BusinessUnitRepository extends CrudRepository<BusinessUnit, Long> {

    List<BusinessUnit> findAllByType(TypeName name);

    List<BusinessUnit> findAllByCompany(BusinessUnit businessUnit);

    List<BusinessUnit> findAllByCompanyAndType(BusinessUnit businessUnit, TypeName typeName);

    List<BusinessUnit> findAllByProject(BusinessUnit businessUnit);
}

package com.company.projectManager.repositories;

import com.company.projectManager.models.Type;
import com.company.projectManager.utils.TypeName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.models.BusinessUnit;

import java.util.List;

@Repository
public interface BusinessUnitRepository extends CrudRepository<BusinessUnit, Long> {

    List<BusinessUnit> findAllByTypeName(TypeName name);

    List<BusinessUnit> findAllByCompany(BusinessUnit businessUnit);

    List<BusinessUnit> findAllByCompanyAndType(BusinessUnit businessUnit, Type type);

    List<BusinessUnit> findAllByProject(BusinessUnit businessUnit);
}

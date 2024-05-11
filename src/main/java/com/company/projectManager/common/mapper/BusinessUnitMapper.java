package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.whiteboard.whiteboards.mapper.WhiteboardMapper;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring", uses = {WhiteboardMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Validated
public interface BusinessUnitMapper {

    @Named("toBusinessUnitDTO")
    default BusinessUnitDTO toBusinessUnitDTO(@Valid BusinessUnit businessUnit) {
        return switch (businessUnit.getType()) {
            case COMPANY -> toCompanyDTO(businessUnit);
            case PROJECT -> toProjectDTO(businessUnit);
            case TEAM -> toTeamDTO(businessUnit);
        };
    }

    default List<BusinessUnitDTO> toBusinessUnitDTO(@Valid Iterable<BusinessUnit> businessUnits) {
        if(businessUnits == null){
            return new ArrayList<>();
        }

        return StreamSupport.stream(businessUnits.spliterator(), false)
                .map(this::toBusinessUnitDTO)
                .collect(Collectors.toList());
    }

    @Named("toCompanyDTO")
    CompanyDTO toCompanyDTO(@Valid BusinessUnit businessUnit);

    @Named("toProjectDTO")
    @Mapping(target = "company", qualifiedByName = {"toCompanyDTO"})
    ProjectDTO toProjectDTO(@Valid BusinessUnit businessUnit);

    @Named("toTeamDTO")
    @Mapping(target = "company", qualifiedByName = {"toCompanyDTO"})
    @Mapping(target = "project", qualifiedByName = {"toProjectDTO"})
    TeamDTO toTeamDTO(@Valid BusinessUnit businessUnit);

    @Named("toBusinessUnitEntity")
    default BusinessUnit toBusinessUnitEntity(@Valid BusinessUnitDTO businessUnitDTO) {
        return switch (businessUnitDTO.type()) {
            case COMPANY -> toEntityFromCompanyDTO((CompanyDTO) businessUnitDTO);
            case PROJECT -> toEntityFromProjectDTO((ProjectDTO) businessUnitDTO);
            case TEAM -> toEntityFromTeamDTO((TeamDTO) businessUnitDTO);
        };
    }

    default List<BusinessUnit> toBusinessUnitEntities(@Valid Iterable<BusinessUnitDTO> businessUnitDTOs) {
        if(businessUnitDTOs == null){
            return new ArrayList<>();
        }

        return StreamSupport.stream(businessUnitDTOs.spliterator(), false)
                .map(this::toBusinessUnitEntity)
                .collect(Collectors.toList());
    }

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "project", ignore = true)
    BusinessUnit toEntityFromCompanyDTO(@Valid CompanyDTO companyDTO);

    @Mapping(target = "project", ignore = true)
    BusinessUnit toEntityFromProjectDTO(@Valid ProjectDTO projectDTO);


    BusinessUnit toEntityFromTeamDTO(@Valid TeamDTO teamDTO);


}
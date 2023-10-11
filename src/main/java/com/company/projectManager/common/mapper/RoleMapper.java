package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.entity.Role;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AuthorityMapper.class, BusinessUnitMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface RoleMapper {

    @Named("toRoleDTO")
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    RoleDTO toDTO(@Valid Role role);

    @IterableMapping(qualifiedByName = "toRoleDTO")
    List<RoleDTO> toDTO(@Valid Iterable<Role> roles);

    @Named("toRoleEntity")
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitEntity"})
    Role toEntity(@Valid RoleDTO role);

    @IterableMapping(qualifiedByName = "toRoleEntity")
    List<Role> toEntity(@Valid Iterable<RoleDTO> roleDTOs);

}

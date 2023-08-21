package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.RoleDTO;
import com.company.projectManager.common.entity.Role;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface RoleMapper {

    @Named("toRoleDTO")
    RoleDTO toDTO(@Valid Role role);

    List<RoleDTO> toDTO(@Valid Iterable<Role> role);

    @Named("toRoleEntity")
    Role toEntity(@Valid RoleDTO role);

    List<Role> toEntity(@Valid Iterable<RoleDTO> roles);

}

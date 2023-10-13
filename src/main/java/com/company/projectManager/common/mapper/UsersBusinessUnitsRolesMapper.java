package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.UserNoPassBusinessUnitRoleDTO;
import com.company.projectManager.common.dto.UserBusinessUnitRoleDTO;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BusinessUnitMapper.class, RoleMapper.class},collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface UsersBusinessUnitsRolesMapper {

    @Mapping(target = "user", qualifiedByName = {"toUserWithoutPasswordDTO"})
    @Mapping(target = "role", qualifiedByName = {"toRoleDTO"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    UserNoPassBusinessUnitRoleDTO toDTO(@Valid UserBusinessUnitRole userBusinessUnitRole);

    List<UserNoPassBusinessUnitRoleDTO> toDTO(@Valid Iterable<UserBusinessUnitRole> usersBusinessUnitsRoles);

    @Mapping(target = "user", qualifiedByName = {"toUserEntity"})
    @Mapping(target = "role", qualifiedByName = {"toRoleEntity"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitEntity"})
    UserBusinessUnitRole toEntity(@Valid UserBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO);

    List<UserBusinessUnitRole> toEntity(@Valid Iterable<UserBusinessUnitRoleDTO> usersWithPassBusinessUnitsRolesDTOs);

}

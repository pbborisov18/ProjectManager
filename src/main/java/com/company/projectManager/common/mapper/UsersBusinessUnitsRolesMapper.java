package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.entity.Authority;
import com.company.projectManager.common.entity.Role;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BusinessUnitMapper.class, AuthorityMapper.class, RoleMapper.class},collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface UsersBusinessUnitsRolesMapper {
    
    @Mapping(target = "user", qualifiedByName = {"toUserWithoutPasswordDTO"})
    @Mapping(target = "role", qualifiedByName = {"toRoleDTO"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    UserNoPassBusinessUnitRoleDTO toDTO(@Valid UserBusinessUnitRole userBusinessUnitRole);

    @Mapping(target = "user", qualifiedByName = {"toUserWithoutPasswordDTO"})
    @Mapping(target = "authorityDTOList", //Expression here basically gets the authorities from the roles
            expression = "java(authoritiesListToString(userBusinessUnitRole.getRole().getAuthorities()))")
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    UserNoPassBusinessUnitAuthoritiesDTO toAuthoritiesDTO(@Valid UserBusinessUnitRole userBusinessUnitRole);

    List<UserNoPassBusinessUnitRoleDTO> toDTO(@Valid Iterable<UserBusinessUnitRole> usersBusinessUnitsRoles);

    List<UserNoPassBusinessUnitAuthoritiesDTO> toAuthoritiesDTO(@Valid Iterable<UserBusinessUnitRole> userBusinessUnitRoles);

    @Mapping(target = "user", qualifiedByName = {"toUserEntity"})
    @Mapping(target = "role", qualifiedByName = {"toRoleEntity"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitEntity"})
    UserBusinessUnitRole toEntity(@Valid UserBusinessUnitRoleDTO userWithPassBusinessUnitRoleDTO);

    List<UserBusinessUnitRole> toEntity(@Valid Iterable<UserBusinessUnitRoleDTO> usersWithPassBusinessUnitsRolesDTOs);

    //Since mapstruct is "clever" and after I tell it to use x dependency
    //it doesn't care and still checks if it needs x dependency.
    //This is used to trick mapstruct into thinking that this impl will need the AuthorityMapper
    @Mapping(target = "authorities", qualifiedByName = {"toAuthoritiesDTO"})
    Role doNotUse(RoleDTO roleDTO);

}

package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.entity.Role;
import com.company.projectManager.common.entity.UserBusinessUnit;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BusinessUnitMapper.class, AuthorityMapper.class, RoleMapper.class},collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface UsersBusinessUnitsMapper {
    
    @Mapping(target = "user", qualifiedByName = {"toUserWithoutPasswordDTO"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    UserNoPassBusinessUnitDTO toDTO(@Valid UserBusinessUnit userBusinessUnit);

    List<UserNoPassBusinessUnitDTO> toDTO(@Valid Iterable<UserBusinessUnit> usersBusinessUnitsRoles);

    @Mapping(target = "user", qualifiedByName = {"toUserWithoutPasswordDTO"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    @Mapping(target = "roles", qualifiedByName = {"toRoleDTO"})
    UserNoPassBusinessUnitRoleDTO toDTOwithRole(@Valid UserBusinessUnit userBusinessUnit);

    List<UserNoPassBusinessUnitRoleDTO> toDTOwithRole(@Valid Iterable<UserBusinessUnit> usersBusinessUnitsRoles);

    //TODO: Better name needed for toAuthoritiesDTO method
    @Mapping(target = "authorityDTOList", //Expression here basically gets the authorities from the roles
            expression = """
                    java(authorityMapper.toDTO(
                    userBusinessUnit.getRoles().stream().flatMap(role -> role.getAuthorities().stream()).distinct().toList()))
                    """)
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    BusinessUnitAuthoritiesDTO toAuthoritiesDTO(@Valid UserBusinessUnit userBusinessUnit);

    List<BusinessUnitAuthoritiesDTO> toAuthoritiesDTO(@Valid Iterable<UserBusinessUnit> userBusinessUnitRoles);

    @Mapping(target = "user", qualifiedByName = {"toUserEntity"})
    @Mapping(target = "roles", source = "roles" ,qualifiedByName = {"toRoleEntity"})
    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitEntity"})
    UserBusinessUnit toEntity(@Valid UserBusinessUnitDTO userWithPassBusinessUnitRoleDTO);

    List<UserBusinessUnit> toEntity(@Valid Iterable<UserBusinessUnitDTO> usersWithPassBusinessUnitsRolesDTOs);

    //Since mapstruct is "clever" and after I tell it to use x dependency
    //it doesn't care and still checks if it needs x dependency.
    //This is used to trick mapstruct into thinking that this impl will need the AuthorityMapper
    @Mapping(target = "authorities", qualifiedByName = {"toAuthoritiesDTO"})
    Role doNotUse(RoleDTO roleDTO);

}

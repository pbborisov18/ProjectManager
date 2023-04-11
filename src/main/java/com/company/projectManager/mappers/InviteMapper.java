package com.company.projectManager.mappers;

import com.company.projectManager.DTOs.InviteDTOWithPassword;
import com.company.projectManager.DTOs.InviteDTOWithoutPassword;
import com.company.projectManager.models.Invite;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BusinessUnitMapper.class},collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface InviteMapper {

    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitDTO"})
    InviteDTOWithoutPassword toDTO(@Valid Invite invite);

    List<InviteDTOWithoutPassword> toDTO(@Valid Iterable<Invite> invites);

    @Mapping(target = "businessUnit", qualifiedByName = {"toBusinessUnitEntity"})
    Invite toEntity(@Valid InviteDTOWithPassword invite);

    List<Invite> toEntity(@Valid Iterable<InviteDTOWithPassword> invites);

}

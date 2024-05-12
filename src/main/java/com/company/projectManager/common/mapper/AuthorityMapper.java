package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.AuthorityDTO;
import com.company.projectManager.common.entity.Authority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
@Validated
public interface AuthorityMapper {

    @Named("toAuthorityDTO")
    AuthorityDTO toDTO(@Valid Authority authority);

//    @IterableMapping(qualifiedByName = "toAuthoritiesDTO")
    List<AuthorityDTO> toDTO(@Valid Iterable<Authority> authorities);

    @Named("toAuthorityEntity")
    Authority toEntity(@Valid AuthorityDTO authorityDTO);


    List<Authority> toEntity(@Valid List<AuthorityDTO> authorities);
}

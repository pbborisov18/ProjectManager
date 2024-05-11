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

    @Named("toAuthoritiesDTO")
    AuthorityDTO toDTO(@Valid @NotNull Authority authority);

    @IterableMapping(qualifiedByName = "toAuthoritiesDTO")
    List<AuthorityDTO> toDTO(@Valid @NotNull Iterable<Authority> authorities);


    Authority toEntity(@Valid @NotNull AuthorityDTO authorityDTO);


    List<Authority> toEntity(@Valid @NotNull List<AuthorityDTO> authorities);
}

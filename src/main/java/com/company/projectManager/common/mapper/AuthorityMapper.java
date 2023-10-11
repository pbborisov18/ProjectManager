package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.AuthorityDTO;
import com.company.projectManager.common.entity.Authority;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface AuthorityMapper {

    AuthorityDTO toDTO(@Valid Authority authority);

    List<AuthorityDTO> toDTO(@Valid Iterable<Authority> authorities);

    Authority toEntity(@Valid AuthorityDTO authorityDTO);

    List<Authority> toEntity(@Valid List<AuthorityDTO> authorities);
}

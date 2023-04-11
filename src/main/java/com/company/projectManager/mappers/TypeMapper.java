package com.company.projectManager.mappers;

import com.company.projectManager.DTOs.TypeDTO;
import com.company.projectManager.models.Type;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface TypeMapper {

    TypeDTO toDTO(@Valid Type type);

    List<TypeDTO> toDTO(@Valid Iterable<Type> types);

    Type toEntity(@Valid TypeDTO typeDTO);

    List<Type> toEntity(@Valid Iterable<TypeDTO> typeDTOs);

}

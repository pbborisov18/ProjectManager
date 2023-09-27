//package com.company.projectManager.common.mapper;
//
//import com.company.projectManager.common.dto.TypeDTO;
//import com.company.projectManager.common.entity.Type;
//import jakarta.validation.Valid;
//import org.mapstruct.CollectionMappingStrategy;
//import org.mapstruct.*;
//import org.springframework.validation.annotation.Validated;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
//@Validated
//public interface TypeMapper {
//
//    TypeDTO toDTO(@Valid Type type);
//
//    List<TypeDTO> toDTO(@Valid Iterable<Type> types);
//
//    Type toEntity(@Valid TypeDTO typeDTO);
//
//    List<Type> toEntity(@Valid Iterable<TypeDTO> typeDTOs);
//
//}

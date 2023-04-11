package com.company.projectManager.mappers;

import com.company.projectManager.DTOs.ColumnDTO;
import com.company.projectManager.models.Column;
import jakarta.validation.Valid;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses = {WhiteboardMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface ColumnMapper {

    @Mapping(target = "whiteboardDTO", source = "whiteboard")
    ColumnDTO toDTO(@Valid Column column);

    List<ColumnDTO> toDTO(@Valid Iterable<Column> columns);

    @Mapping(target = "whiteboard", source = "whiteboardDTO")
    Column toEntity(@Valid ColumnDTO columnDTO);

    List<Column> toEntity(@Valid Iterable<ColumnDTO> columnDTOs);
}

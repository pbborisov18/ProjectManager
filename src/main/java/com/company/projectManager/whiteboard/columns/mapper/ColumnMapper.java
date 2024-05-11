package com.company.projectManager.whiteboard.columns.mapper;

import com.company.projectManager.whiteboard.whiteboards.mapper.WhiteboardMapper;
import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses = {WhiteboardMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
@Validated
public interface ColumnMapper {

    @Mapping(target = "whiteboardDTO", source = "whiteboard")
    ColumnDTO toDTO(@Valid @NotNull Column column);

    List<ColumnDTO> toDTO(@Valid @NotNull Iterable<Column> columns);

    @Mapping(target = "whiteboard", source = "whiteboardDTO")
    Column toEntity(@Valid @NotNull ColumnDTO columnDTO);

    List<Column> toEntity(@Valid @NotNull Iterable<ColumnDTO> columnDTOs);
}

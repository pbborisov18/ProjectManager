package com.company.projectManager.whiteboard.whiteboards.mapper;

import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface WhiteboardMapper {

    WhiteboardDTO toDTO(@Valid @NotNull Whiteboard whiteboard);

    List<WhiteboardDTO> toDTO(@Valid @NotNull Iterable<Whiteboard> whiteboards);

    Whiteboard toEntity(@Valid @NotNull WhiteboardDTO whiteboardDTO);

    List<Whiteboard> toEntity(@Valid @NotNull Iterable<WhiteboardDTO> whiteboardDTOs);

}

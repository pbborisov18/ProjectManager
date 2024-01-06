package com.company.projectManager.whiteboard.whiteboards.mapper;

import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface WhiteboardMapper {

    WhiteboardDTO toDTO(@Valid Whiteboard whiteboard);

    List<WhiteboardDTO> toDTO(@Valid Iterable<Whiteboard> whiteboards);

    Whiteboard toEntity(@Valid WhiteboardDTO whiteboardDTO);

    List<Whiteboard> toEntity(@Valid Iterable<WhiteboardDTO> whiteboardDTOs);

}

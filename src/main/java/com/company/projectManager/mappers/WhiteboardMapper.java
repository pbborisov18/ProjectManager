package com.company.projectManager.mappers;

import com.company.projectManager.DTOs.WhiteboardDTO;
import com.company.projectManager.models.Whiteboard;
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

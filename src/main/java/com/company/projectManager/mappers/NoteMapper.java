package com.company.projectManager.mappers;

import com.company.projectManager.DTOs.NoteDTO;
import com.company.projectManager.models.Note;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses={ColumnMapper.class},collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface NoteMapper {

    @Mapping(target = "columnDTO", source = "column")
    NoteDTO toDTO(@Valid Note note);

    List<NoteDTO> toDTO(@Valid Iterable<Note> notes);

    @Mapping(target = "column", source = "columnDTO")
    Note toEntity(@Valid NoteDTO noteDTO);

    List<Note> toEntity(@Valid Iterable<NoteDTO> noteDTOs);

}

package com.company.projectManager.whiteboard.notes.mapper;

import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.entity.Note;
import com.company.projectManager.whiteboard.columns.mapper.ColumnMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper(componentModel = "spring", uses={ColumnMapper.class},collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface NoteMapper {

    @Mapping(target = "columnDTO", source = "column")
    NoteDTO toDTO(@Valid @NotNull Note note);

    List<NoteDTO> toDTO(@Valid @NotNull Iterable<Note> notes);

    @Mapping(target = "column", source = "columnDTO")
    Note toEntity(@Valid @NotNull NoteDTO noteDTO);

    List<Note> toEntity(@Valid @NotNull Iterable<NoteDTO> noteDTOs);

}

package com.company.projectManager.whiteboard.notes.mapper;

import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import com.company.projectManager.whiteboard.columns.mapper.ColumnMapper;
import com.company.projectManager.whiteboard.notes.dto.NoteDTO;
import com.company.projectManager.whiteboard.notes.entity.Note;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

//This is WebMvcTest to limit the context to as little as possible but still start up spring
//as the validation is imported by that. (If you find a way to import the validation
//without booting up spring go ahead and make the changes)
@WebMvcTest(NoteMapper.class)
class NoteMapperTest {

    private static Column column1;
    private static Column column2;
    private static ColumnDTO columnDTO1;
    private static ColumnDTO columnDTO2;

    @MockBean
    private ColumnMapper columnMapper;

    private final NoteMapper noteMapper;

    @Autowired
    public NoteMapperTest(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @BeforeAll
    public static void setUp() {
        column1 = new Column(1L, "Column 1", new Whiteboard(1L, "Whiteboard 1"), 1L);
        column2 = new Column(2L, "Column 2", new Whiteboard(2L, "Whiteboard 2"), 2L);

        columnDTO1 = new ColumnDTO(1L, "Column 1", new WhiteboardDTO(1L, "Whiteboard 1"), 1L);
        columnDTO2 = new ColumnDTO(2L, "Column 2", new WhiteboardDTO(2L, "Whiteboard 2"), 2L);
    }

    @Test
    @DisplayName("Test toDTO with valid Note entity")
    public void testToDTO() {
        //given
        when(columnMapper.toDTO(Mockito.any(Column.class)))
                .thenReturn(columnDTO1);
        Note note = new Note(1L, "Note 1", "Note 1 Body", column1, 1L);

        //when
        NoteDTO toCompare = noteMapper.toDTO(note);

        //then
        verify(columnMapper, times(1)).toDTO(Mockito.any(Column.class));
        assertThat(toCompare)
                .isEqualTo(new NoteDTO(
                        note.getId(),
                        note.getName(),
                        note.getDescription(),
                        columnDTO1,
                        note.getPosition()
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid Note entity")
    //given
    @MethodSource("provideInvalidNote")
    public void testToDTOInvalid(Note note) {
        //when and then
        assertThatThrownBy(() -> noteMapper.toDTO(note))
                .isInstanceOf(ConstraintViolationException.class);
    }

    //A little brittle test but eh
    @ParameterizedTest
    @DisplayName("Test toDTO with valid Note entity list")
    //given
    @MethodSource("provideValidNotes")
    public void testToDTOList(List<Note> notes) {
        //given
        when(columnMapper.toDTO(Mockito.any(Column.class)))
                .thenAnswer( invocatin -> {
                    Column column = invocatin.getArgument(0);
                    return new ColumnDTO(
                            column.getId(),
                            column.getName(),
                            new WhiteboardDTO(
                                    column.getWhiteboard().getId(),
                                    column.getWhiteboard().getName()
                            ),
                            column.getPosition());
                });



        //when
        List<NoteDTO> toCompare = noteMapper.toDTO(notes);

        //then
        verify(columnMapper, times(2)).toDTO(Mockito.any(Column.class));
        assertThat(toCompare)
                .isEqualTo(List.of(
                        new NoteDTO(
                                notes.get(0).getId(),
                                notes.get(0).getName(),
                                notes.get(0).getDescription(),
                                new ColumnDTO(
                                        notes.get(0).getColumn().getId(),
                                        notes.get(0).getColumn().getName(),
                                        new WhiteboardDTO(
                                                notes.get(0).getColumn().getWhiteboard().getId(),
                                                notes.get(0).getColumn().getWhiteboard().getName()
                                        ),
                                        notes.get(0).getColumn().getPosition()
                                ),
                                notes.get(0).getPosition()
                        ),
                        new NoteDTO(
                                notes.get(1).getId(),
                                notes.get(1).getName(),
                                notes.get(1).getDescription(),
                                new  ColumnDTO(
                                        notes.get(1).getColumn().getId(),
                                        notes.get(1).getColumn().getName(),
                                        new WhiteboardDTO(
                                                notes.get(1).getColumn().getWhiteboard().getId(),
                                                notes.get(1).getColumn().getWhiteboard().getName()
                                        ),
                                        notes.get(1).getColumn().getPosition()
                                ),
                                notes.get(1).getPosition()
                        )

                ));
    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid Note entity list")
    //given
    @MethodSource("provideInvalidNotes")
    public void testToDTOListInvalid(List<Note> notes) {
        //when and then
        assertThatThrownBy(() -> noteMapper.toDTO(notes))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toEntity with valid NoteDTO")
    public void testToEntity() {
        //given
        when(columnMapper.toEntity(Mockito.any(ColumnDTO.class)))
                .thenReturn(column1);

        NoteDTO noteDTO = new NoteDTO(1L, "Note 1", "Note 1 Body",
                                        columnDTO1, 1L);

        //when
        Note toCompare = noteMapper.toEntity(noteDTO);

        //then
        verify(columnMapper, times(1)).toEntity(Mockito.any(ColumnDTO.class));
        assertThat(toCompare)
                .isEqualTo(new Note(
                        noteDTO.id(),
                        noteDTO.name(),
                        noteDTO.description(),
                        column1,
                        noteDTO.position()
                ));
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid NoteDTO")
    //given
    @MethodSource("provideInvalidNoteDTO")
    public void testToEntityInvalid(NoteDTO noteDTO) {
        //when and then
        assertThatThrownBy(() -> noteMapper.toEntity(noteDTO))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with valid NoteDTO list")
    //given
    @MethodSource("provideValidNoteDTOs")
    public void testToEntityList(List<NoteDTO> noteDTOs) {
        //given
        when(columnMapper.toEntity(Mockito.any(ColumnDTO.class)))
                .thenAnswer( invocatin -> {
                    ColumnDTO columnDTO = invocatin.getArgument(0);
                    return new Column(
                            columnDTO.id(),
                            columnDTO.name(),
                            new Whiteboard(
                                    columnDTO.whiteboardDTO().id(),
                                    columnDTO.whiteboardDTO().name()
                            ),
                            columnDTO.position());
                });

        //when
        List<Note> toCompare = noteMapper.toEntity(noteDTOs);

        //then
        verify(columnMapper, times(2)).toEntity(Mockito.any(ColumnDTO.class));
        assertThat(toCompare)
                .isEqualTo(List.of(
                        new Note(
                                noteDTOs.get(0).id(),
                                noteDTOs.get(0).name(),
                                noteDTOs.get(0).description(),
                                new Column(
                                        noteDTOs.get(0).columnDTO().id(),
                                        noteDTOs.get(0).columnDTO().name(),
                                        new Whiteboard(
                                                noteDTOs.get(0).columnDTO().whiteboardDTO().id(),
                                                noteDTOs.get(0).columnDTO().whiteboardDTO().name()
                                        ),
                                        noteDTOs.get(0).columnDTO().position()
                                ),
                                noteDTOs.get(0).position()
                        ),
                        new Note(
                                noteDTOs.get(1).id(),
                                noteDTOs.get(1).name(),
                                noteDTOs.get(1).description(),
                                new Column(
                                        noteDTOs.get(1).columnDTO().id(),
                                        noteDTOs.get(1).columnDTO().name(),
                                        new Whiteboard(
                                                noteDTOs.get(1).columnDTO().whiteboardDTO().id(),
                                                noteDTOs.get(1).columnDTO().whiteboardDTO().name()
                                        ),
                                        noteDTOs.get(1).columnDTO().position()
                                ),
                                noteDTOs.get(1).position()
                        )
                ));
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid NoteDTO list")
    //given
    @MethodSource("provideInvalidNoteDTOs")
    public void testToEntityListInvalid(List<NoteDTO> noteDTOs) {
        //when and then
        assertThatThrownBy(() -> noteMapper.toEntity(noteDTOs))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Note> provideInvalidNote() {
        return Stream.of(
                new Note(),
                new Note(1L, null, "Note 1 Body", column1, 1L),
                new Note(1L, "", "Note 1 Body", column1, 1L),
                new Note(1L, "Note 1", null, column1, 1L),
                new Note(1L, "Note 1", "", column1, 1L),
                new Note(1L, "Note 1", "Note 1 Body", null, 1L),
                new Note(1L, "Note 1", "Note 1 Body", column1, null)
        );
    }

    static Stream<List<Note>> provideValidNotes() {
        return Stream.of(
                List.of(
                        new Note(1L, "Note 1", "Note 1 Body", column1, 1L),
                        new Note(2L, "Note 2", "Note 2 Body", column1, 2L)
                ),
                List.of(
                        new Note(1L, "Note 1", "Note 1 Body", column1, 1L),
                        new Note(2L, "Note 2", "Note 2 Body", column2, 1L)
                )
        );
    }

    static Stream<List<Note>> provideInvalidNotes(){
        return Stream.of(
                //All being invalid
                List.of(
                        new Note(),
                        new Note(1L, null, "Note 1 Body", column1, 1L)
                ),
                //One invalid
                List.of(
                        new Note(),
                        new Note(1L, "Note 1", "valid", column1, 1L),
                        new Note(1L, "Note 1", "valid", column1, 1L)
                ),

                null
        );
    }

    static Stream<NoteDTO> provideInvalidNoteDTO() {
        return Stream.of(
                new NoteDTO(1L, null, "Note 1 Body", columnDTO1, 1L),
                new NoteDTO(1L, "", "Note 1 Body", columnDTO1, 1L),
                new NoteDTO(1L, "Note 1", null, columnDTO1, 1L),
                new NoteDTO(1L, "Note 1", "", columnDTO1, 1L),
                new NoteDTO(1L, "Note 1", "Note 1 Body", null, 1L),
                new NoteDTO(1L, "Note 1", "Note 1 Body", columnDTO1, null)
        );
    }

    static Stream<List<NoteDTO>> provideValidNoteDTOs() {
        return Stream.of(
                List.of(
                        new NoteDTO(1L, "Note 1", "Note 1 Body", columnDTO1, 1L),
                        new NoteDTO(2L, "Note 2", "Note 2 Body", columnDTO1, 2L)
                ),
                List.of(
                        new NoteDTO(1L, "Note 1", "Note 1 Body", columnDTO1, 1L),
                        new NoteDTO(2L, "Note 2", "Note 2 Body", columnDTO2, 1L)
                )
        );
    }

    static Stream<List<NoteDTO>> provideInvalidNoteDTOs(){
        return Stream.of(
                //All being invalid
                List.of(
                        new NoteDTO(1L, null, "Note 1 Body", columnDTO1, 1L),
                        new NoteDTO(1L, "", "Note 1 Body", columnDTO1, 1L)
                ),
                //One invalid
                List.of(
                        new NoteDTO(1L, null, "invalid", columnDTO1, 1L),
                        new NoteDTO(1L, "Note 1", "valid", columnDTO1, 1L)
                ),

                null
        );
    }

}
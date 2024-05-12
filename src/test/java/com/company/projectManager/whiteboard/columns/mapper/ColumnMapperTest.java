package com.company.projectManager.whiteboard.columns.mapper;

import com.company.projectManager.whiteboard.columns.dto.ColumnDTO;
import com.company.projectManager.whiteboard.columns.entity.Column;
import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import com.company.projectManager.whiteboard.whiteboards.mapper.WhiteboardMapper;
import jakarta.validation.ConstraintViolationException;
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
@WebMvcTest(ColumnMapper.class)
class ColumnMapperTest {

    @MockBean
    private WhiteboardMapper whiteboardMapper;

    private final ColumnMapper columnMapper;

    @Autowired
    public ColumnMapperTest(ColumnMapper columnMapper) {
        this.columnMapper = columnMapper;
    }

    @Test
    @DisplayName("Test toDTO with valid Column entity")
    public void testToDTO() {
        //given
        when(whiteboardMapper.toDTO(Mockito.any(Whiteboard.class)))
                .thenReturn(new WhiteboardDTO(1L, "whiteboard1"));

        Column column = new Column(1L, "column1", new Whiteboard(1L, "whiteboard1"), 1L);

        //when
        ColumnDTO toCompare = columnMapper.toDTO(column);

        //then
        verify(whiteboardMapper, times(1)).toDTO(Mockito.any(Whiteboard.class));
        assertThat(toCompare)
                .isEqualTo(new ColumnDTO(
                                        column.getId(),
                                        column.getName(),
                                        new WhiteboardDTO(column.getWhiteboard().getId(),column.getWhiteboard().getName()),
                                        column.getPosition()
                        )
                );
    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid Column entity")
    //given
    @MethodSource("provideInvalidColumn")
    public void testToDTOInvalid(Column column) {
        //when and then
        assertThatThrownBy(() -> columnMapper.toDTO(column))
                .isInstanceOf(ConstraintViolationException.class);
    }

    //A little brittle test but eh...
    @ParameterizedTest
    @DisplayName("Test toDTO with valid list of Column entities")
    //given
    @MethodSource("provideValidColumns")
    public void testToDTOListDifferentWhiteboards(List<Column> columns) {
        //given
        when(whiteboardMapper.toDTO(Mockito.any(Whiteboard.class)))
                .thenAnswer( invocation -> {
                        Whiteboard whiteboard = invocation.getArgument(0);
                        return new WhiteboardDTO(whiteboard.getId(), whiteboard.getName());
                });

        //when
        List<ColumnDTO> columnDTOs = columnMapper.toDTO(columns);

        //then
        verify(whiteboardMapper, times(2)).toDTO(Mockito.any(Whiteboard.class));
        assertThat(columnDTOs)
                .isEqualTo(List.of(new ColumnDTO(
                                        columns.get(0).getId(),
                                        columns.get(0).getName(),
                                        new WhiteboardDTO(columns.get(0).getWhiteboard().getId(), columns.get(0).getWhiteboard().getName()),
                                        columns.get(0).getPosition()
                                ),
                                new ColumnDTO(
                                        columns.get(1).getId(),
                                        columns.get(1).getName(),
                                        new WhiteboardDTO(columns.get(1).getWhiteboard().getId(), columns.get(1).getWhiteboard().getName()),
                                        columns.get(1).getPosition()
                                )
                        )
                );

    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid list of Column entities")
    //given
    @MethodSource("provideInvalidColumns")
    public void testToDTOListInvalid(List<Column> columns) {
        //when and then
        assertThatThrownBy(() -> columnMapper.toDTO(columns))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toEntity with valid ColumnDTO")
    public void testToEntity() {
        //given
        when(whiteboardMapper.toEntity(Mockito.any(WhiteboardDTO.class)))
                .thenReturn(new Whiteboard(1L, "whiteboard1"));

        ColumnDTO columnDTO = new ColumnDTO(1L, "column1", new WhiteboardDTO(1L, "whiteboard1"), 1L);

        //when
        Column toCompare = columnMapper.toEntity(columnDTO);

        //then
        verify(whiteboardMapper, times(1)).toEntity(Mockito.any(WhiteboardDTO.class));
        assertThat(toCompare)
                .isEqualTo(new Column(
                        columnDTO.id(),
                        columnDTO.name(),
                        new Whiteboard(columnDTO.whiteboardDTO().id(), columnDTO.whiteboardDTO().name()),
                        columnDTO.position())
                );
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid ColumnDTO")
    //given
    @MethodSource("provideInvalidColumnDTO")
    public void testToEntityInvalid(ColumnDTO columnDTO) {
        //when and then
        assertThatThrownBy(() -> columnMapper.toEntity(columnDTO))
                .isInstanceOf(ConstraintViolationException.class);
    }

    //A little brittle test but eh...
    @ParameterizedTest
    @DisplayName("Test toEntity with valid list of ColumnDTOs")
    //given
    @MethodSource("provideValidColumnDTOs")
    public void testToEntityListDifferentWhiteboards(List<ColumnDTO> columnDTOs) {
        //given
        when(whiteboardMapper.toEntity(Mockito.any(WhiteboardDTO.class)))
                .thenAnswer(invocation -> {
                    WhiteboardDTO whiteboardDTO = invocation.getArgument(0);
                    return new Whiteboard(whiteboardDTO.id(), whiteboardDTO.name());
                });

        //when
        List<Column> toCompare = columnMapper.toEntity(columnDTOs);

        //then
        verify(whiteboardMapper, times(2)).toEntity(Mockito.any(WhiteboardDTO.class));
        assertThat(toCompare)
                .isEqualTo(List.of(
                                new Column(
                                        columnDTOs.get(0).id(),
                                        columnDTOs.get(0).name(),
                                        new Whiteboard(columnDTOs.get(0).whiteboardDTO().id(),columnDTOs.get(0).whiteboardDTO().name()),
                                        columnDTOs.get(0).position()),
                                new Column(
                                        columnDTOs.get(1).id(),
                                        columnDTOs.get(1).name(),
                                        new Whiteboard(columnDTOs.get(1).whiteboardDTO().id(),columnDTOs.get(1).whiteboardDTO().name()),
                                        columnDTOs.get(1).position()
                                )
                        )
                );

    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid list of ColumnDTOs")
    //given
    @MethodSource("provideInvalidColumnDTOs")
    public void testToEntityListInvalid(List<ColumnDTO> columnDTOs) {
        //when and then
        assertThatThrownBy(() -> columnMapper.toEntity(columnDTOs))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Column> provideInvalidColumn() {
        return Stream.of(
                new Column(1L, null, new Whiteboard(1L, "whiteboard1"), 1L),
                new Column(1L, "", new Whiteboard(1L, "whiteboard1"), 1L),
                new Column(1L, "column1", null, 1L),
                new Column(1L, "column1", new Whiteboard(1L, "whiteboard1"), null),
                new Column(null, null, null, null)
        );
    }

    static Stream<List<Column>> provideValidColumns(){

        return Stream.of(
                List.of(
                        new Column(1L, "column1", new Whiteboard(1L, "whiteboard1"), 1L),
                        new Column(2L, "column2", new Whiteboard(1L, "whiteboard1"), 2L)
                ),
                List.of(
                        new Column(1L, "column1", new Whiteboard(1L, "whiteboard1"), 1L),
                        new Column(2L, "column2", new Whiteboard(2L, "whiteboard2"), 1L)
                )
        );
    }

    static Stream<List<Column>> provideInvalidColumns(){
        return Stream.of(
                //Only 1 being valid
                List.of(
                        new Column(1L, null, new Whiteboard(1L, "whiteboard1"), 1L),
                        new Column(2L, "valid", new Whiteboard(1L, "whiteboard1"), 1L)
                ),
                //All being invalid
                List.of(
                        new Column(1L, null, new Whiteboard(1L, "whiteboard1"), 1L),
                        new Column(1L, null, new Whiteboard(1L, "whiteboard1"), 1L)
                )
        );
    }

    static Stream<ColumnDTO> provideInvalidColumnDTO() {
        return Stream.of(
                new ColumnDTO(1L, null, new WhiteboardDTO(1L, "whiteboard1"), 1L),
                new ColumnDTO(1L, "", new WhiteboardDTO(1L, "whiteboard1"), 1L),
                new ColumnDTO(1L, "column1", null, 1L),
                new ColumnDTO(1L, "column1", new WhiteboardDTO(1L, "whiteboard1"), null),
                new ColumnDTO(null, null, null, null)
        );
    }

    static Stream<List<ColumnDTO>> provideValidColumnDTOs(){

        return Stream.of(
                List.of(
                        new ColumnDTO(1L, "column1", new WhiteboardDTO(1L, "whiteboard1"), 1L),
                        new ColumnDTO(2L, "column2", new WhiteboardDTO(1L, "whiteboard1"), 2L)
                ),
                List.of(
                        new ColumnDTO(1L, "column1", new WhiteboardDTO(1L, "whiteboard1"), 1L),
                        new ColumnDTO(2L, "column2", new WhiteboardDTO(2L, "whiteboard2"), 1L)
                )
        );
    }

    static Stream<List<ColumnDTO>> provideInvalidColumnDTOs(){
        return Stream.of(
                //Only 1 being valid
                List.of(
                        new ColumnDTO(1L, null, new WhiteboardDTO(1L, "whiteboard1"), 1L),
                        new ColumnDTO(2L, "valid", new WhiteboardDTO(1L, "whiteboard1"), 1L)
                ),
                //All being invalid
                List.of(
                        new ColumnDTO(1L, null, new WhiteboardDTO(1L, "whiteboard1"), 1L),
                        new ColumnDTO(1L, null, new WhiteboardDTO(1L, "whiteboard1"), 1L)
                )
        );
    }

}
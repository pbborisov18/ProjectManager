package com.company.projectManager.whiteboard.whiteboards.mapper;

import com.company.projectManager.whiteboard.whiteboards.dto.WhiteboardDTO;
import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//This is WebMvcTest to limit the context to as little as possible but still start up spring
//as the validation is imported by that. (If you find a way to import the validation
//without booting up spring go ahead and make the changes)
@WebMvcTest(WhiteboardMapper.class)
public class WhiteboardMapperTest {

    private final WhiteboardMapper whiteboardMapper;

    @Autowired
    public WhiteboardMapperTest(WhiteboardMapper whiteboardMapper) {
        this.whiteboardMapper = whiteboardMapper;
    }

    @Test
    @DisplayName("Test toDTO with valid Whiteboard entity")
    public void testToDTO() {
        //given
        Whiteboard whiteboard1 = new Whiteboard(1L, "whiteboard1");

        //when
        WhiteboardDTO toCompare = whiteboardMapper.toDTO(whiteboard1);

        //then
        assertThat(toCompare).isEqualTo(new WhiteboardDTO(1L, "whiteboard1"));
    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid Whiteboard entity")
    //given
    @MethodSource("provideInvalidWhiteboard")
    public void testToDTOInvalid(Whiteboard whiteboard) {
        //when and then
        assertThatThrownBy(() -> whiteboardMapper.toDTO(whiteboard))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toDTO with valid list of Whiteboard entities")
    public void testToDTOList() {
        //given
        Whiteboard whiteboard1 = new Whiteboard(1L, "whiteboard1");
        Whiteboard whiteboard2 = new Whiteboard(2L, "whiteboard2");

        //when
        List<WhiteboardDTO> toCompare1 = whiteboardMapper.toDTO(List.of(whiteboard1, whiteboard2));

        //then
        assertThat(toCompare1).containsExactlyInAnyOrder(
                new WhiteboardDTO(1L, "whiteboard1"),
                new WhiteboardDTO(2L, "whiteboard2")
        );
    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid list of Whiteboard entities")
    //given
    @MethodSource("provideInvalidWhiteboards")
    public void testToDTOListInvalid(List<Whiteboard> whiteboards) {
        //when and then
        assertThatThrownBy(() -> whiteboardMapper.toDTO(whiteboards))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toEntity with valid WhiteboardDTO")
    public void testToEntity() {
        //given
        WhiteboardDTO whiteboardDTO1 = new WhiteboardDTO(1L, "whiteboardDTO1");

        //when
        Whiteboard toCompare = whiteboardMapper.toEntity(whiteboardDTO1);

        //then
        assertThat(toCompare).isEqualTo(new Whiteboard(1L, "whiteboardDTO1"));
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid WhiteboardDTO")
    //given
    @MethodSource("provideInvalidWhiteboardDTO")
    public void testToEntityInvalid(WhiteboardDTO whiteboardDTO) {
        //when and then
        assertThatThrownBy(() -> whiteboardMapper.toEntity(whiteboardDTO))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toEntity with valid list of WhiteboardDTOs")
    public void testToEntityList() {
        //given
        WhiteboardDTO whiteboardDTO1 = new WhiteboardDTO(1L, "whiteboardDTO1");
        WhiteboardDTO whiteboardDTO2 = new WhiteboardDTO(2L, "whiteboardDTO2");

        //when
        List<Whiteboard> toCompare1 = whiteboardMapper.toEntity(List.of(whiteboardDTO1, whiteboardDTO2));

        //then
        assertThat(toCompare1).containsExactlyInAnyOrder(new Whiteboard(1L, "whiteboardDTO1"), new Whiteboard(2L, "whiteboardDTO2"));
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid list of WhiteboardDTOs")
    //given
    @MethodSource("provideInvalidWhiteboardDTOs")
    public void testToEntityListInvalid(List<WhiteboardDTO> whiteboardDTOs) {
        //when and then
        assertThatThrownBy(() -> whiteboardMapper.toEntity(whiteboardDTOs))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Whiteboard> provideInvalidWhiteboard() {
        return Stream.of(
                new Whiteboard(null, null),
                new Whiteboard(1L, null),
                null
        );
    }

    static Stream<List<Whiteboard>> provideInvalidWhiteboards() {
        return Stream.of(
                //Only 1 being invalid
                List.of(new Whiteboard(1L, "whiteboard1"),
                        new Whiteboard(2L, null)
                ),

                //All invalid
                List.of(new Whiteboard(null, null),
                        new Whiteboard(null, null)
                ),

                //First being invalid
                List.of(new Whiteboard(1L, null),
                        new Whiteboard(2L, "whiteboard2")
                ),

                null
        );
    }

    static Stream<WhiteboardDTO> provideInvalidWhiteboardDTO() {
        return Stream.of(
                new WhiteboardDTO(null, null),
                new WhiteboardDTO(1L, null),
                null
        );
    }

    static Stream<List<WhiteboardDTO>> provideInvalidWhiteboardDTOs() {
        return Stream.of(
                //Only 1 being invalid
                List.of(new WhiteboardDTO(1L, "whiteboard1"),
                        new WhiteboardDTO(2L, null)
                ),

                //All invalid
                List.of(new WhiteboardDTO(null, null),
                        new WhiteboardDTO(null, null)
                ),

                //First being invalid
                List.of(new WhiteboardDTO(1L, null),
                        new WhiteboardDTO(2L, "whiteboard2")
                ),

                null
        );
    }

}

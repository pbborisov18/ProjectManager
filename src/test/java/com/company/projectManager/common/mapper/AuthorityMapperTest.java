package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.AuthorityDTO;
import com.company.projectManager.common.entity.Authority;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

//This is WebMvcTest to limit the context to as little as possible but still start up spring
//as the validation is imported by that. (If you find a way to import the validation
//without booting up spring go ahead and make the changes)
@WebMvcTest(AuthorityMapper.class)
public class AuthorityMapperTest {

    private final AuthorityMapper authorityMapper;

    @Autowired
    public AuthorityMapperTest(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    @Test
    @DisplayName("Test toDTO with valid Authority entity")
    public void testToDTO() {
        //given
        Authority auth1 = new Authority(1L, "auth1");

        //when
        AuthorityDTO toCompare = authorityMapper.toDTO(auth1);

        //then
        assertThat(toCompare).isEqualTo(new AuthorityDTO(1L, "auth1"));
    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid Authority entity")
    //given
    @MethodSource("provideInvalidAuthority")
    public void testToDTOInvalid(Authority authority) {
        //when and then
        assertThatThrownBy(() -> authorityMapper.toDTO(authority))
                .isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    @DisplayName("Test toDTO with valid list of Authority entities")
    public void testToDTOList() {
        //given
        Authority auth1 = new Authority(1L, "auth1");
        Authority auth2 = new Authority(2L, "auth2");

        //when
        List<AuthorityDTO> toCompare1 = authorityMapper.toDTO(List.of(auth1, auth2));

        //then
        assertThat(toCompare1).containsExactlyInAnyOrder(
                new AuthorityDTO(1L, "auth1"),
                new AuthorityDTO(2L, "auth2")
        );

    }

    @ParameterizedTest
    @DisplayName("Test toDTO with invalid list of Authority entities")
    //given
    @MethodSource("provideInvalidAuthorities")
    public void testToDTOListInvalid(List<Authority> authorities) {
        //when and then
        assertThatThrownBy(() -> authorityMapper.toDTO(authorities))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toEntity with valid AuthorityDTO")
    public void testToEntity() {
        //given
        AuthorityDTO auth1 = new AuthorityDTO(1L, "auth1");

        //when
        Authority toCompare = authorityMapper.toEntity(auth1);

        //then
        assertThat(toCompare).isEqualTo(new Authority(1L, "auth1"));
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid AuthorityDTO")
    //given
    @MethodSource("provideInvalidAuthorityDTO")
    public void testToEntityInvalid(AuthorityDTO authority) {
        //when and then
        assertThatThrownBy(() -> authorityMapper.toEntity(authority))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Test toEntity with valid list of AuthorityDTOs")
    public void testToEntityList() {
        //given
        AuthorityDTO auth1 = new AuthorityDTO(1L, "auth1");
        AuthorityDTO auth2 = new AuthorityDTO(2L, "auth2");

        //when
        List<Authority> toCompare1 = authorityMapper.toEntity(List.of(auth1, auth2));

        //then
        assertThat(toCompare1).isEqualTo(List.of(new Authority(1L, "auth1"), new Authority(2L, "auth2")));
    }

    @ParameterizedTest
    @DisplayName("Test toEntity with invalid list of AuthorityDTOs")
    //given
    @MethodSource("provideInvalidAuthorityDTOs")
    public void testToEntityListInvalid(List<AuthorityDTO> authorities) {
        //when and then
        assertThatThrownBy(() -> authorityMapper.toEntity(authorities))
                .isInstanceOf(ConstraintViolationException.class);
    }

    static Stream<Authority> provideInvalidAuthority() {
        return Stream.of(
                new Authority(null, null),
                new Authority(1L, null),
                null
        );
    }

    static Stream<List<Authority>> provideInvalidAuthorities() {
        return Stream.of(
                //Only 1 being invalid
                List.of(new Authority(1L, "authority1"),
                        new Authority(2L, null)
                ),

                //All invalid
                List.of(new Authority(null, null),
                        new Authority(null, null)
                ),

                //First being invalid
                List.of(new Authority(1L, null),
                        new Authority(2L, "authority2")
                ),

                null
        );
    }

    static Stream<AuthorityDTO> provideInvalidAuthorityDTO() {
        return Stream.of(
                new AuthorityDTO(null, null),
                new AuthorityDTO(1L, null),
                null
        );
    }

    static Stream<List<AuthorityDTO>> provideInvalidAuthorityDTOs() {
        return Stream.of(
                //Only 1 being invalid
                List.of(new AuthorityDTO(1L, "authority1"),
                        new AuthorityDTO(2L, null)
                ),

                //All invalid
                List.of(new AuthorityDTO(null, null),
                        new AuthorityDTO(null, null)
                ),

                //First being invalid
                List.of(new AuthorityDTO(1L, null),
                        new AuthorityDTO(2L, "authority2")
                ),

                null
        );
    }
}

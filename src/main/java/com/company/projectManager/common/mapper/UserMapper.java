package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.UserDTO;
import com.company.projectManager.common.dto.UserWithoutPasswordDTO;
import com.company.projectManager.common.entity.User;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface UserMapper {

    UserDTO toUserDTO(@Valid User user);

    @Named("toUserWithoutPasswordDTO")
    UserWithoutPasswordDTO toUserWithoutPasswordDTO(@Valid User user);

    List<UserWithoutPasswordDTO> toUserWithoutPasswordDTO(@Valid Iterable<User> users);

    @Named("toUserEntity")
    User toEntity(@Valid UserDTO userDTO);

    List<User> toEntity(@Valid Iterable<User> users);

}

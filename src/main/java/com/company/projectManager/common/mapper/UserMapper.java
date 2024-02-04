package com.company.projectManager.common.mapper;

import com.company.projectManager.common.dto.user.UserDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.entity.User;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
@Validated
public interface UserMapper {

    @Named("toUserWithoutPasswordDTO")
    UserNoPassDTO toUserWithoutPasswordDTO(@Valid User user);

    List<UserNoPassDTO> toUserWithoutPasswordDTO(@Valid Iterable<User> users);

    @Named("toUserEntity")
    User toEntity(@Valid UserDTO userDTO);

    List<User> toEntity(@Valid Iterable<User> users);

}

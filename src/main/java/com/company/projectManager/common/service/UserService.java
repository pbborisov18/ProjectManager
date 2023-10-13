package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.UserDTO;
import com.company.projectManager.common.dto.UserNoPassDTO;
import com.company.projectManager.common.exception.*;

public interface UserService {

    void saveUser(UserDTO userDTO) throws FailedToSaveException;

    void updateUser(UserNoPassDTO userNoPassDTO) throws FailedToUpdateException, EntityNotFoundException;

    void updateUser(UserDTO userDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteUser(UserDTO userDTO) throws FailedToDeleteException, EntityNotFoundException;

    UserNoPassDTO findUserById(Long id) throws FailedToSelectException, EntityNotFoundException;

    UserNoPassDTO findUserByEmail(String email) throws FailedToSelectException, EntityNotFoundException;

    void register(UserDTO userDTO) throws UserAlreadyExistsException, FailedToSaveException;
}

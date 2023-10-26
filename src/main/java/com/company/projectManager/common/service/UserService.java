package com.company.projectManager.common.service;

import com.company.projectManager.common.dto.UserDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.exception.*;

public interface UserService {

    void updateUser(UserNoPassDTO userNoPassDTO) throws FailedToUpdateException, EntityNotFoundException;

    void updateUser(UserDTO userDTO) throws FailedToUpdateException, EntityNotFoundException;

    void deleteAuthenticatedUser() throws FailedToDeleteException, EntityNotFoundException;

    UserNoPassDTO findUserByEmail(String email) throws FailedToSelectException, EntityNotFoundException;

    //No save method as basically the only time you'd be doing save is during registration
    void register(UserDTO userDTO) throws UserAlreadyExistsException, FailedToSaveException;
}

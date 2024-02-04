package com.company.projectManager.common.service;


import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.exception.*;


import java.util.List;

public interface UsersBusinessUnitsService {

    List<UserNoPassDTO> findTheLast30UsersWhoJoinedBU(Long businessUnitId) throws FailedToSelectException;

    UserNoPassBusinessUnitRoleDTO findUserRoles(Long businessUnitId, String email) throws FailedToSelectException, EntityNotFoundException;

    void saveUserRoles(UserNoPassBusinessUnitRoleDTO ubuDTO) throws FailedToSaveException, EntityNotFoundException;

    void kickFromBU(String email, Long businessUnitId) throws EntityNotFoundException, FailedToDeleteException;

}

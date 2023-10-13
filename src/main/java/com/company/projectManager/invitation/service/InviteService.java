package com.company.projectManager.invitation.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserNoPassDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTOWithoutPassword;

import java.util.List;

public interface InviteService {

    List<InviteDTOWithoutPassword> findInvitesByAuthenticatedReceiver(InviteState inviteState) throws FailedToSelectException;

    List<InviteDTOWithoutPassword> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException;

    void createInviteByAuthenticatedUser(BusinessUnitDTO businessUnitDTO, UserWithoutPasswordDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException;

    void createInviteByAuthenticatedUser(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException;

}

package com.company.projectManager.invitation.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserNoPassDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTONoPass;

import java.util.List;

public interface InviteService {

    List<InviteDTONoPass> findInvitesByAuthenticatedUserAndState(InviteState inviteState) throws FailedToSelectException;

    List<InviteDTONoPass> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException;

    void acceptInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException, UserNotAuthorizedException;

    void declineInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException;

    void cancelInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToDeleteException;

    void createInvite(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException;

}

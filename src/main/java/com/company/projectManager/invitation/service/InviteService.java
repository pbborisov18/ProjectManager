package com.company.projectManager.invitation.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserNoPassDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTONoPass;

import java.util.List;

public interface InviteService {

    List<InviteDTONoPass> findInvitesByAuthenticatedReceiver(InviteState inviteState) throws FailedToSelectException;

    List<InviteDTONoPass> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException;

    void acceptInvite(InviteDTONoPass inviteDTONoPass);

    void cancelInvite(InviteDTONoPass inviteDTONoPass);

    void declineInvite(InviteDTONoPass inviteDTONoPass);

    void updateInviteByAuthenticatedUser(InviteDTONoPass inviteDTONoPass) throws InvalidInvitationException, UserNotAuthorizedException, FailedToUpdateException, FailedToSelectException, UserUnauthenticatedException;

    void createInviteByAuthenticatedUser(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException;

}

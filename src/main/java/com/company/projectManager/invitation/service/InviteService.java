package com.company.projectManager.invitation.service;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTONoPass;

import java.util.List;

public interface InviteService {

    List<InviteDTONoPass> findInvitesByAuthenticatedUserAndState(InviteState inviteState) throws FailedToSelectException;

    List<InviteDTONoPass> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, EntityNotFoundException;

    void acceptInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException, UserNotAuthorizedException;

    void declineInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException;

    void cancelInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToDeleteException;

    void createInvite(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws InvalidInvitationException, FailedToSaveException;

}

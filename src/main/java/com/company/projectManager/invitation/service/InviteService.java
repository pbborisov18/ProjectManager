package com.company.projectManager.invitation.service;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.invitation.dto.InviteDTOWithPassword;
import com.company.projectManager.invitation.dto.InviteDTOWithoutPassword;
import com.company.projectManager.common.dto.UserWithoutPasswordDTO;
import com.company.projectManager.invitation.mapper.InviteMapper;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.invitation.entity.Invite;
import com.company.projectManager.common.entity.Role;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.invitation.repository.InviteRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.common.utils.RoleName;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public interface InviteService {

    void saveInvite(InviteDTOWithoutPassword inviteDTOWithoutPassword) throws InvalidInvitationException, FailedToSaveException;

    void updateInviteByAuthenticatedUser(InviteDTOWithoutPassword inviteDTOWithoutPassword) throws InvalidInvitationException, UserNotAuthorizedException, FailedToUpdateException, FailedToSelectException, UserUnauthenticatedException;

    List<InviteDTOWithoutPassword> findInvitesByAuthenticatedReceiver() throws FailedToSelectException, UserUnauthenticatedException, EntityNotFoundException;

    List<InviteDTOWithoutPassword> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException;

    void createInviteByAuthenticatedUser(BusinessUnitDTO businessUnitDTO, UserWithoutPasswordDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException;

    void deleteAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException;
}

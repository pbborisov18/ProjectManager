package com.company.projectManager.invitation.service.impl;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTONoPass;
import com.company.projectManager.invitation.entity.Invite;
import com.company.projectManager.invitation.mapper.InviteMapper;
import com.company.projectManager.invitation.repository.InviteRepository;
import com.company.projectManager.invitation.service.InviteService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;

    private final InviteMapper inviteMapper;

    private final UserRepository userRepository;

    private final BusinessUnitMapper businessUnitMapper;

    private final UsersBusinessUnitsRolesRepository userBURoleRepository;

    public InviteServiceImpl(InviteRepository inviteRepository, InviteMapper inviteMapper, UserRepository userRepository, BusinessUnitMapper businessUnitMapper, UsersBusinessUnitsRolesRepository userBURoleRepository) {
        this.inviteRepository = inviteRepository;
        this.inviteMapper = inviteMapper;
        this.userRepository = userRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.userBURoleRepository = userBURoleRepository;
    }

    public List<InviteDTONoPass> findInvitesByAuthenticatedUserAndState(InviteState inviteState) throws FailedToSelectException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            return inviteMapper.toDTO(
                    inviteRepository.findByReceiver_EmailAndState(email, inviteState));

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    public List<InviteDTONoPass> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findUserByEmail(email);

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBURoleEntity.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't part of the BusinessUnit");
            }
            //-----------------

            List<Invite> invites = inviteRepository.findAllByBusinessUnitId(businessUnitDTO.id());

            if(invites.isEmpty()){
                throw new EntityNotFoundException("No invites found!");
            }

            return inviteMapper.toDTO(invites);
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    @Transactional
    public void acceptInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException, UserNotAuthorizedException {
        try {
            Optional<Invite> invite = inviteRepository.findById(inviteDTONoPass.id());

            if (invite.isEmpty()) {
                throw new EntityNotFoundException("Invite not found");
            }

            if(!inviteDTONoPass.receiver().email().equals(
                    invite.get().getReceiver().getEmail())){
                throw new UserNotAuthorizedException("Invite was sent to someone else");
            }

            invite.get().setState(InviteState.ACCEPTED);

            //Keep the invite in the db so a user can't be invited to the same BU that they are already in
            //Make sure to (manually) cascade delete the invites
            inviteRepository.save(invite.get());

            //TODO: to revisit when authorization is done
            //Can't do cascade persist cuz that will break the merge so will have to be done manually...
//            userBURoleRepository.save(new UserBusinessUnit(null,
//                                                                invite.get().getReceiver(),
//                                                                invite.get().getBusinessUnit(),
//                                                                new Role(null,
//                                                                        "Default",
//                                                                        List.of(new Authority(null, "Default")),
//                                                                        invite.get().getBusinessUnit())));
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    public void declineInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException {
        try {
            Optional<Invite> invite = inviteRepository.findById(inviteDTONoPass.id());

            if (invite.isEmpty()) {
                throw new EntityNotFoundException("Invite not found");
            }

            invite.get().setState(InviteState.DECLINED);

            //Keep the invite in the db so the user can't be spammed the same invite
            //Basically if you decline it acts as block (until you remove the invite)
            inviteRepository.save(invite.get());
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    public void cancelInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToDeleteException {
        try {
            Optional<Invite> invite = inviteRepository.findById(inviteDTONoPass.id());

            if (invite.isEmpty()) {
                throw new EntityNotFoundException("Invite not found");
            }

            //Delete the invite in case
            inviteRepository.delete(invite.get());
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }


    public void createInvite(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, InvalidInvitationException, FailedToSaveException {
        try {
            //AUTHENTICATION (Already done in the security config) AND AUTHORIZATION (To be moved)
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findUserByEmail(email);

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnit> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBURoleEntity.isEmpty()) {
                throw new UserNotInBusinessUnitException("User isn't part of the BusinessUnit");
            }
            //-----------------

            Optional<User> receiverEntity = userRepository.findUserByEmail(receiver.email());

            if(receiverEntity.isEmpty()){
                throw new InvalidInvitationException("User doesn't exist");
            }

            Optional<Invite> inviteExists = inviteRepository.findInviteByBusinessUnit_IdAndReceiver_Email(businessUnitDTO.id(), receiver.email());

            if(inviteExists.isPresent() ||
                    receiver.email().equals(
                            SecurityContextHolder.getContext().getAuthentication().getName())){
                throw new InvalidInvitationException("Invite already exists!");
            }

            Invite invite = new Invite(null,
                    InviteState.PENDING,
                    receiverEntity.get(),
                    businessUnitMapper.toBusinessUnitEntity(businessUnitDTO));

            inviteRepository.save(invite);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }

}

package com.company.projectManager.invitation.service.impl;

import com.company.projectManager.common.dto.businessUnit.BusinessUnitDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.entity.BusinessUnit;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnit;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.repository.RoleRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRepository;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.invitation.dto.InviteDTONoPass;
import com.company.projectManager.invitation.entity.Invite;
import com.company.projectManager.invitation.mapper.InviteMapper;
import com.company.projectManager.invitation.repository.InviteRepository;
import com.company.projectManager.invitation.service.InviteService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;

    private final InviteMapper inviteMapper;


    private final UserRepository userRepository;


    private final BusinessUnitMapper businessUnitMapper;

    private final UsersBusinessUnitsRepository usersBURepository;
    private final RoleRepository roleRepository;

    public InviteServiceImpl(InviteRepository inviteRepository, InviteMapper inviteMapper, UserRepository userRepository, BusinessUnitMapper businessUnitMapper,
                             UsersBusinessUnitsRepository usersBURepository, RoleRepository roleRepository) {
        this.inviteRepository = inviteRepository;
        this.inviteMapper = inviteMapper;
        this.userRepository = userRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.usersBURepository = usersBURepository;
        this.roleRepository = roleRepository;
    }

    public List<InviteDTONoPass> findInvitesByAuthenticatedUserAndState(InviteState inviteState) throws FailedToSelectException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            return inviteMapper.toDTO(
                    inviteRepository.findByReceiverEmailIgnoreCaseAndState(email, inviteState));

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    public List<InviteDTONoPass> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
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

            usersBURepository.save(
                    new UserBusinessUnit(null,
                            invite.get().getReceiver(),
                            invite.get().getBusinessUnit(),
                            //Get the default role in the business unit
                            List.of(roleRepository.findByNameAndBusinessUnitId("Default", invite.get().getBusinessUnit().getId())
                                    .get())));

            if(invite.get().getBusinessUnit().getType() == TypeName.TEAM){
                acceptInvitesToParents(invite.get().getReceiver(), invite.get().getBusinessUnit().getCompany());
                acceptInvitesToParents(invite.get().getReceiver(), invite.get().getBusinessUnit().getProject());
            } else if (invite.get().getBusinessUnit().getType() == TypeName.PROJECT){
                acceptInvitesToParents(invite.get().getReceiver(), invite.get().getBusinessUnit().getCompany());
            }


        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e){
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void acceptInvitesToParents(User user, BusinessUnit businessUnit) throws FailedToUpdateException {
        try {

            Invite invite = new Invite(null, InviteState.ACCEPTED, user, businessUnit);

            //Keep the invite in the db so a user can't be invited to the same BU that they are already in
            //Make sure to (manually) cascade delete the invites
            inviteRepository.save(invite);

            usersBURepository.save(
                    new UserBusinessUnit(null,
                            invite.getReceiver(),
                            invite.getBusinessUnit(),
                            //Get the default role in the business unit
                            List.of(roleRepository.findByNameAndBusinessUnitId("Default", invite.getBusinessUnit().getId())
                                    .get())));

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e){
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    public void declineInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToUpdateException, UserNotAuthorizedException {
        try {
            Optional<Invite> invite = inviteRepository.findById(inviteDTONoPass.id());

            if (invite.isEmpty()) {
                throw new EntityNotFoundException("Invite not found");
            }

            if(!inviteDTONoPass.receiver().email().equals(
                    invite.get().getReceiver().getEmail())){
                throw new UserNotAuthorizedException("Invite was sent to someone else");
            }

            invite.get().setState(InviteState.DECLINED);

            //Keep the invite in the db so the user can't be spammed the same invite
            //Basically if you decline it acts as block (until you remove the invite)
            inviteRepository.save(invite.get());
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    //Delete from receiver
    public void deleteInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, UserNotAuthorizedException, FailedToDeleteException {
        try {
            Optional<Invite> invite = inviteRepository.findById(inviteDTONoPass.id());

            if (invite.isEmpty()) {
                throw new EntityNotFoundException("Invite not found");
            }

            if(!inviteDTONoPass.receiver().email().equals(
                    invite.get().getReceiver().getEmail())){
                throw new UserNotAuthorizedException("Invite was sent to someone else");
            }

            //Delete invite. Acts as ignore
            //User should be able to receive the same invite for the same bu afterwards
            inviteRepository.delete(invite.get());
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to update! " + e.getMessage());
        }
    }

    //Delete from bu(sender)
    @PreAuthorize("authorityCheck(#inviteDTONoPass.businessUnit().id(), \"ManageSentInvites\")")
    public void cancelInvite(InviteDTONoPass inviteDTONoPass) throws EntityNotFoundException, FailedToDeleteException {
        try {
            Optional<Invite> invite = inviteRepository.findById(inviteDTONoPass.id());

            if (invite.isEmpty()) {
                throw new EntityNotFoundException("Invite not found");
            }

            //Delete the invite in case you want to invite the user again
            inviteRepository.delete(invite.get());
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }


    public InviteDTONoPass createInvite(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws InvalidInvitationException, FailedToSaveException {
        try {
            Optional<User> receiverEntity = userRepository.findUserByEmailIgnoreCase(receiver.email());

            if(receiverEntity.isEmpty()){
                throw new InvalidInvitationException("User doesn't exist");
            }

            Optional<Invite> inviteExists = inviteRepository.findByReceiverEmailIgnoreCaseAndBusinessUnitId(receiver.email(), businessUnitDTO.id());

            //Checks if invite already exists. Or the user is being a smartass trying to invite themselves
            if(inviteExists.isPresent() ||
                    receiver.email().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
                throw new InvalidInvitationException("Invalid invite!");
            }

            Invite invite = new Invite(null,
                    InviteState.PENDING,
                    receiverEntity.get(),
                    businessUnitMapper.toBusinessUnitEntity(businessUnitDTO));

            Invite i = inviteRepository.save(invite);

            return inviteMapper.toDTO(i);
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }

}

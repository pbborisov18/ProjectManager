package com.company.projectManager.services;

import com.company.projectManager.DTOs.BusinessUnitDTO;
import com.company.projectManager.DTOs.InviteDTOWithPassword;
import com.company.projectManager.DTOs.InviteDTOWithoutPassword;
import com.company.projectManager.DTOs.UserWithoutPasswordDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.mappers.InviteMapper;
import com.company.projectManager.mappers.UserMapper;
import com.company.projectManager.models.Invite;
import com.company.projectManager.models.Role;
import com.company.projectManager.models.User;
import com.company.projectManager.models.UserBusinessUnitRole;
import com.company.projectManager.repositories.InviteRepository;
import com.company.projectManager.repositories.UserRepository;
import com.company.projectManager.repositories.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.utils.InviteState;
import com.company.projectManager.utils.RoleName;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public abstract class InviteService {

    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    InviteMapper inviteMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UsersBusinessUnitsRolesRepository userBURoleRepository;


    public void saveInvite(InviteDTOWithoutPassword inviteDTOWithoutPassword) throws InvalidInvitationException, FailedToSaveException {
        try {
            if(inviteDTOWithoutPassword.sender().equals(inviteDTOWithoutPassword.receiver())){
                throw new InvalidInvitationException("You can't send an invite to yourself!");
            }

            Optional<User> receiver = userRepository.findUserByEmail(inviteDTOWithoutPassword.receiver().email());
            Optional<User> sender = userRepository.findUserByEmail(inviteDTOWithoutPassword.sender().email());

            if(receiver.isPresent() && sender.isPresent()) {
                InviteDTOWithPassword inviteDTOWithPassword = new InviteDTOWithPassword(
                        inviteDTOWithoutPassword.id(),
                        InviteState.PENDING,
                        userMapper.toUserDTO(sender.get()),
                        userMapper.toUserDTO(receiver.get()),
                        inviteDTOWithoutPassword.businessUnit()
                );

                inviteRepository.save(inviteMapper.toEntity(inviteDTOWithPassword));
            } else {
                throw new InvalidInvitationException("Either sender or receiver doesn't exist!");
            }

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    public void updateInviteByAuthenticatedUser(InviteDTOWithoutPassword inviteDTOWithoutPassword) throws InvalidInvitationException, UserNotAuthorizedException, FailedToUpdateException, FailedToSelectException, UserUnauthenticatedException {
        try {
            Optional<User> loggedInUser = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(loggedInUser.isEmpty()){
                throw new UserUnauthenticatedException("User is unauthenticated");
            }

            Optional<Invite> existingInvite = inviteRepository.findById(inviteDTOWithoutPassword.id());

            if(existingInvite.isEmpty()){
                throw new FailedToSelectException("Invite doesn't exist!");
            }

            Optional<User> receiver = userRepository.findUserByEmail(inviteDTOWithoutPassword.receiver().email());
            Optional<User> sender = userRepository.findUserByEmail(inviteDTOWithoutPassword.sender().email());

            if(receiver.isEmpty() || sender.isEmpty()){
                //Means someone has deleted their profile
                inviteRepository.delete(existingInvite.get());
                throw new InvalidInvitationException("Either sender or receiver doesn't exist");
            }

            //Receiver can change the state to whatever
            if(loggedInUser.get().getEmail().equals(sender.get().getEmail())){
                //authenticated sender
                if(inviteDTOWithoutPassword.state() != InviteState.CANCELLED){
                    throw new UserNotAuthorizedException("You can only cancel the invite");
                }
            } else if (loggedInUser.get().getEmail().equals(receiver.get().getEmail())){
                // in case you need to stuff in the future. I don't think I can do anything rn.
            } else {
                throw new UserNotAuthorizedException("You aren't the receiver nor sender!");
            }

            //Only update the state to avoid letting the users change stuff I don't want them to
            existingInvite.get().setState(inviteDTOWithoutPassword.state());

            inviteRepository.save(existingInvite.get());

            if(existingInvite.get().getState().equals(InviteState.ACCEPTED)){
                userBURoleRepository.save(new UserBusinessUnitRole(
                        receiver.get(),
                        existingInvite.get().getBusinessUnit(),
                        new Role(2L, RoleName.EMPLOYEE)
                ));
            }

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToUpdateException("Unsuccessful update! " + e.getMessage());
        }
    }

    public void deleteInvite(InviteDTOWithoutPassword inviteDTOWithoutPassword) throws FailedToSelectException, FailedToDeleteException {
        try{
            Optional<Invite> existingInvite = inviteRepository.findById(inviteDTOWithoutPassword.id());

            if(existingInvite.isEmpty()){
                throw new FailedToSelectException("Invite doesn't exist!");
            }

            inviteRepository.delete(existingInvite.get());

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }

    public InviteDTOWithoutPassword findInviteById(Long id) throws FailedToSelectException {
        try {
            Optional<Invite> existingInvite = inviteRepository.findById(id);

            if(existingInvite.isEmpty()){
                throw new FailedToSelectException("Invite was not found");
            }

            return inviteMapper.toDTO(existingInvite.get());

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    public List<InviteDTOWithoutPassword> findInvitesByReceiverId(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            List<Invite> existingInvites = inviteRepository.findByReceiverIdAndStateNotIn(id);

            if(existingInvites.isEmpty()){
                throw new EntityNotFoundException("Invite was not found");
            }

            return inviteMapper.toDTO(existingInvites);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    public List<InviteDTOWithoutPassword> findInvitesByAuthenticatedReceiverId() throws FailedToSelectException, UserUnauthenticatedException, EntityNotFoundException {
        try {
            Optional<User> user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()){
              throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            return findInvitesByReceiverId(user.get().getId());

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    public void createInviteByAuthenticatedUser(BusinessUnitDTO businessUnitDTO, UserWithoutPasswordDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException {
        try {
            Optional<User> sender = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            if(sender.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            } else {
                Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(sender.get().getId(), businessUnitDTO.id());

                if(userBURoleEntity.isEmpty()){
                    throw new UserNotInBusinessUnitException("User isn't part of the BusinessUnit");
                } else if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
                    throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
                } else {
                    saveInvite(
                        new InviteDTOWithoutPassword(
                            null,
                            InviteState.PENDING,
                            userMapper.toUserWithoutPasswordDTO(sender.get()),
                            receiver,
                            businessUnitDTO
                        )
                    );
                }
            }

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    public List<InviteDTOWithoutPassword> findAllInvites() throws FailedToSelectException {
        try {
            List<Invite> existingInvites = (List<Invite>) inviteRepository.findAll();

            if(existingInvites.isEmpty()){
                throw new FailedToSelectException("Invites were not found");
            }

            return inviteMapper.toDTO(existingInvites);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }


}

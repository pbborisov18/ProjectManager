package com.company.projectManager.invitation.service.impl;

import com.company.projectManager.common.dto.BusinessUnitDTO;
import com.company.projectManager.common.dto.UserNoPassDTO;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.utils.InviteState;
import com.company.projectManager.invitation.dto.InviteDTOWithoutPassword;
import com.company.projectManager.invitation.entity.Invite;
import com.company.projectManager.invitation.mapper.InviteMapper;
import com.company.projectManager.invitation.repository.InviteRepository;
import com.company.projectManager.invitation.service.InviteService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserMapper userMapper;

    UsersBusinessUnitsRolesRepository userBURoleRepository;

    public InviteServiceImpl(InviteRepository inviteRepository, InviteMapper inviteMapper, UserRepository userRepository, UserMapper userMapper, UsersBusinessUnitsRolesRepository userBURoleRepository) {
        this.inviteRepository = inviteRepository;
        this.inviteMapper = inviteMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userBURoleRepository = userBURoleRepository;
    }

    @Transactional
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

    @Transactional
    public void updateInviteByAuthenticatedUser(InviteDTOWithoutPassword inviteDTONoPass) throws InvalidInvitationException, UserNotAuthorizedException, FailedToUpdateException, FailedToSelectException, UserUnauthenticatedException {
        try {
            Optional<User> loggedInUser = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(loggedInUser.isEmpty()){
                throw new UserUnauthenticatedException("User is unauthenticated");
            }

            Optional<Invite> existingInvite = inviteRepository.findById(inviteDTONoPass.id());

            if(existingInvite.isEmpty()){
                throw new FailedToSelectException("Invite doesn't exist!");
            }

            Optional<User> receiver = userRepository.findUserByEmail(inviteDTOWithoutPassword.receiver().email());
            Optional<User> sender = userRepository.findUserByEmail(inviteDTOWithoutPassword.sender().email());
            Optional<User> receiver = userRepository.findUserByEmail(inviteDTONoPass.receiver().email());
            Optional<User> sender = userRepository.findUserByEmail(inviteDTONoPass.sender().email());

            if(receiver.isEmpty() || sender.isEmpty()){
                //Means someone has deleted their profile
                inviteRepository.delete(existingInvite.get());
                throw new InvalidInvitationException("Either sender or receiver doesn't exist");
            }

            //Receiver can change the state to whatever
            if(loggedInUser.get().getEmail().equals(sender.get().getEmail())){
                //authenticated sender
                if(inviteDTONoPass.state() != InviteState.CANCELLED){
                    throw new UserNotAuthorizedException("You can only cancel the invite");
                }
            } else if (loggedInUser.get().getEmail().equals(receiver.get().getEmail())){
                // in case you need to stuff in the future. I don't think I can do anything rn.
            } else {
                throw new UserNotAuthorizedException("You aren't the receiver nor sender!");
            }

            //Only update the state to avoid letting the users change stuff I don't want them to
            existingInvite.get().setState(inviteDTONoPass.state());

            inviteRepository.save(existingInvite.get());
            //TODO: This has to be reworked
            if(existingInvite.get().getState().equals(InviteState.ACCEPTED)){
                userBURoleRepository.save(new UserBusinessUnitRole(
                        null,
                        receiver.get(),
                        existingInvite.get().getBusinessUnit(),
                        null
                ));
            }

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToUpdateException("Unsuccessful update! " + e.getMessage());
        }
    }

//    @Transactional
//    public void deleteInvite(InviteDTOWithoutPassword inviteDTOWithoutPassword) throws FailedToSelectException, FailedToDeleteException {
//        try{
//            Optional<Invite> existingInvite = inviteRepository.findById(inviteDTOWithoutPassword.id());
//
//            if(existingInvite.isEmpty()){
//                throw new FailedToSelectException("Invite doesn't exist!");
//            }
//
//            inviteRepository.delete(existingInvite.get());
//
//        } catch (ConstraintViolationException | DataAccessException e){
//            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
//        }
//    }
//
//    @Transactional
//    public InviteDTOWithoutPassword findInviteById(Long id) throws FailedToSelectException {
//        try {
//            Optional<Invite> existingInvite = inviteRepository.findById(id);
//
//            if(existingInvite.isEmpty()){
//                throw new FailedToSelectException("Invite was not found");
//            }
//
//            return inviteMapper.toDTO(existingInvite.get());
//
//        } catch (ConstraintViolationException | DataAccessException e){
//            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
//        }
//    }
//
//  out
//    @Transactional
//    public List<InviteDTOWithoutPassword> findInvitesByReceiverId(Long id) throws FailedToSelectException, EntityNotFoundException {
//        try {
//            List<Invite> existingInvites = inviteRepository.findByReceiverIdAndStatePending(id);
//
//            if(existingInvites.isEmpty()){
//                throw new EntityNotFoundException("Invite was not found");
//            }
//
//            return inviteMapper.toDTO(existingInvites);
//
//        } catch (ConstraintViolationException | DataAccessException e){
//            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
//        }
//    }

    //Checked
    @Transactional
    public List<InviteDTOWithoutPassword> findInvitesByAuthenticatedReceiver() throws FailedToSelectException, UserUnauthenticatedException, EntityNotFoundException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findUserByEmail(email);

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            return inviteMapper.toDTO(
                    inviteRepository.findByReceiverIdAndStatePending(user.get().getId()));

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
        }
    }

    @Transactional
    public List<InviteDTOWithoutPassword> findAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToSelectException, UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, EntityNotFoundException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findUserByEmail(email);

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBURoleEntity.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't part of the BusinessUnit");
            } else if(userBURoleEntity.get().getRole().getName() != RoleName.MANAGER){
                throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
            }

            List<Invite> invites = inviteRepository.findAllByBusinessUnitId(businessUnitDTO.id());

            if(invites.isEmpty()){
                throw new EntityNotFoundException("No invites found!");
            }

            return inviteMapper.toDTO(invites);
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSelectException("Failed select! " + e.getMessage());
        }
    }


    //Checked
    //TODO: If an invite is sent to an already invited person isn't handled correctly (letting the db throw a unique key collision)
    @Transactional
    public void createInviteByAuthenticatedUser(BusinessUnitDTO businessUnitDTO, UserNoPassDTO receiver) throws UserUnauthenticatedException, UserNotInBusinessUnitException, UserNotAuthorizedException, InvalidInvitationException, FailedToSaveException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userRepository.findUserByEmail(email);

            if(user.isEmpty()){
                throw new UserUnauthenticatedException("User isn't authenticated!");
            }

            Optional<UserBusinessUnitRole> userBURoleEntity = userBURoleRepository.findByUserIdAndBusinessUnitId(user.get().getId(), businessUnitDTO.id());

            if(userBURoleEntity.isEmpty()){
                throw new UserNotInBusinessUnitException("User isn't part of the BusinessUnit");
            } else if (userBURoleEntity.get().getRole().getName() != RoleName.MANAGER) {
                throw new UserNotAuthorizedException("User doesn't have the necessary permissions!");
            }

            saveInvite( new InviteDTOWithoutPassword(
                    null,
                    InviteState.PENDING,
                    userMapper.toUserWithoutPasswordDTO(user.get()),
                    receiver,
                    businessUnitDTO
            ));

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    //I think this should be fixed with cascades ffs.
    //TODO: Maybe fix with cascades
    @Transactional
    public void deleteAllInvitesByBusinessUnit(BusinessUnitDTO businessUnitDTO) throws FailedToDeleteException {
        try {

            List<Invite> invites = inviteRepository.findAllByBusinessUnitId(businessUnitDTO.id());

            if(invites.isEmpty()){
                return;
            }

            inviteRepository.deleteAll(invites);
        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }


//    @Transactional
//    public List<InviteDTOWithoutPassword> findAllInvites() throws FailedToSelectException {
//        try {
//            List<Invite> existingInvites = (List<Invite>) inviteRepository.findAll();
//
//            if(existingInvites.isEmpty()){
//                throw new FailedToSelectException("Invites were not found");
//            }
//
//            return inviteMapper.toDTO(existingInvites);
//
//        } catch (ConstraintViolationException | DataAccessException e){
//            throw new FailedToSelectException("Unsuccessful select! " + e.getMessage());
//        }
//    }

}

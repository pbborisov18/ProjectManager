package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.user.UserDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRepository;
import com.company.projectManager.common.security.SecurityUser;
import com.company.projectManager.common.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public void updateUser(UserNoPassDTO userNoPassDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<User> existingUser = userRepository.findById(userNoPassDTO.id());

            if(existingUser.isEmpty()) {
                throw new EntityNotFoundException("User not found");
            }

            existingUser.get().setEmail(userNoPassDTO.email());

            userRepository.save(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    public void updateUser(UserDTO userDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<User> existingUser = userRepository.findById(userDTO.id());

            if(existingUser.isEmpty()) {
                throw new EntityNotFoundException("User not found!");
            }

            existingUser.get().setEmail(userDTO.email());
            //encode the password manually
            existingUser.get().setPassword(passwordEncoder.encode(userDTO.password()));

            userRepository.save(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    //TODO: Add (manual) cascade deleting all UserBusinessUnits and Invites that contain the user
    //TODO: Invalidate current session
    //Only an authenticated user can delete their own account
    public void deleteAuthenticatedUser() throws FailedToDeleteException, EntityNotFoundException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> existingUser = userRepository.findUserByEmail(email);

            if(existingUser.isEmpty()){
                throw new EntityNotFoundException("User not found!");
            }

            userRepository.delete(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    public UserNoPassDTO findUserByEmail(String email) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<User> existingUser = userRepository.findUserByEmail(email);

            if(existingUser.isEmpty()){
                throw new EntityNotFoundException("User not found!");
            }

            return userMapper.toUserWithoutPasswordDTO(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    public void register(UserDTO userDTO) throws UserAlreadyExistsException, FailedToSaveException {
        try {
            if(checkIfUserExists(userDTO.email())){
                throw new UserAlreadyExistsException("User already exists with this email!");
            }
            User user = userMapper.toEntity(userDTO);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Failed to register! " + e.getMessage());
        }
    }

    private boolean checkIfUserExists(String email){
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    @Transactional //Transactional so I can get the role ids
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(username);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Email not found " + username);
        }

        return new SecurityUser(user.get(), new ArrayList<>());
    }

}

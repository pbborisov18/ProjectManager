package com.company.projectManager.services;

import com.company.projectManager.DTOs.UserDTO;
import com.company.projectManager.DTOs.UserWithoutPasswordDTO;
import com.company.projectManager.exceptions.*;
import com.company.projectManager.mappers.UserMapper;
import com.company.projectManager.models.User;
import com.company.projectManager.models.UserBusinessUnitRole;
import com.company.projectManager.repositories.RoleRepository;
import com.company.projectManager.repositories.UserRepository;
import com.company.projectManager.repositories.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.security.SecurityRole;
import com.company.projectManager.security.SecurityUser;
import com.company.projectManager.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public abstract  class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;


    @Transactional
    public void saveUser(UserDTO userDTO) throws FailedToSaveException {
        try {
            User user = userMapper.toEntity(userDTO);

            userRepository.save(user);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSaveException("Unsuccessful save! " + e.getMessage());
        }
    }

    @Transactional
    public void updateUser(UserWithoutPasswordDTO userWithoutPasswordDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<User> existingUser = userRepository.findById(userWithoutPasswordDTO.id());

            if(existingUser.isEmpty()) {
                throw new EntityNotFoundException("User not found");
            }

            existingUser.get().setEmail(userWithoutPasswordDTO.email());

            userRepository.save(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Unsuccessful update! " + e.getMessage());
        }
    }

    @Transactional
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
            throw new FailedToUpdateException("Unsuccessful update! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteUser(UserDTO userDTO) throws FailedToDeleteException, EntityNotFoundException {
        try {
            Optional<User> existingUser = userRepository.findById(userDTO.id());

            if(existingUser.isEmpty()){
                throw new EntityNotFoundException("User not found!");
            }

            userRepository.delete(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Unsuccessful delete! " + e.getMessage());
        }
    }


    @Transactional
    public UserWithoutPasswordDTO findUserById(Long id) throws FailedToSelectException, EntityNotFoundException {
        try {
            Optional<User> existingUser = userRepository.findById(id);

            if(existingUser.isEmpty()) {
                throw new EntityNotFoundException("User not found!");
            }

            return userMapper.toUserWithoutPasswordDTO(existingUser.get());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Unsuccessful select!" + e.getMessage());
        }
    }

    @Transactional
    public UserWithoutPasswordDTO findUserByEmail(String email) throws FailedToSelectException, EntityNotFoundException {
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



    @Transactional
    public void register(UserDTO userDTO) throws UserAlreadyExistsException, FailedToSaveException {

        if(checkIfUserExists(userDTO.email())){
            throw new UserAlreadyExistsException("User already exists with this email!");
        }

        saveUser(new UserDTO(
                null,
                userDTO.email(),
                passwordEncoder.encode(userDTO.password())
                )
        );

    }

    private boolean checkIfUserExists(String email){
        return userRepository.findUserByEmail(email).isPresent();
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(username);

        if(user.isPresent()){
            List<UserBusinessUnitRole> userBURole = usersBusinessUnitsRolesRepository.findAllByUserId(user.get().getId());

            List<GrantedAuthority> roles = new ArrayList<>();

            for (UserBusinessUnitRole ubr : userBURole) {
                SecurityRole securityRole = new SecurityRole(ubr.getRole(), ubr.getBusinessUnit());
                roles.add(securityRole);
            }

            return new SecurityUser(user.get(), roles);
        }

        throw new UsernameNotFoundException("Email not found " + username);
    }


}

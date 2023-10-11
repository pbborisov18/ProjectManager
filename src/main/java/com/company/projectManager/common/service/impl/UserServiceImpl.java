package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.UserDTO;
import com.company.projectManager.common.dto.UserWithoutPasswordDTO;
import com.company.projectManager.common.entity.User;
import com.company.projectManager.common.entity.UserBusinessUnitRole;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.repository.RoleRepository;
import com.company.projectManager.common.repository.UserRepository;
import com.company.projectManager.common.repository.UsersBusinessUnitsRolesRepository;
import com.company.projectManager.common.security.SecurityIds;
import com.company.projectManager.common.security.SecurityUser;
import com.company.projectManager.common.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UsersBusinessUnitsRolesRepository usersBusinessUnitsRolesRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.usersBusinessUnitsRolesRepository = usersBusinessUnitsRolesRepository;
    }

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
                SecurityIds securityRole = new SecurityIds(ubr.getId(), ubr.getUser().getId(), ubr.getBusinessUnit().getId(), ubr.getRole().getId());
                roles.add(securityRole);
            }

            return new SecurityUser(user.get(), roles);
        }

        throw new UsernameNotFoundException("Email not found " + username);
    }

}

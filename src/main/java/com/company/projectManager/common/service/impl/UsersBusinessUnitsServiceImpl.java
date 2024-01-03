package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.dto.user.UserNoPassDTO;
import com.company.projectManager.common.entity.*;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.mapper.RoleMapper;
import com.company.projectManager.common.mapper.UserMapper;
import com.company.projectManager.common.mapper.UsersBusinessUnitsMapper;
import com.company.projectManager.common.repository.*;
import com.company.projectManager.common.service.UsersBusinessUnitsService;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.invitation.repository.InviteRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UsersBusinessUnitsServiceImpl implements UsersBusinessUnitsService {

    private final UsersBusinessUnitsRepository usersBURepository;

    private final UsersBusinessUnitsMapper usersBUMapper;


    private final UserRepository userRepository;

    private final UserMapper userMapper;


    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitMapper businessUnitMapper;


    private final InviteRepository inviteRepository;


    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;


    private final AuthoritityRepository authoritityRepository;

    public UsersBusinessUnitsServiceImpl(UsersBusinessUnitsRepository usersBURepository, UsersBusinessUnitsMapper usersBUMapper, UserRepository userRepository, UserMapper userMapper, BusinessUnitMapper businessUnitMapper, BusinessUnitRepository businessUnitRepository, InviteRepository inviteRepository,
                                         RoleRepository roleRepository, RoleMapper roleMapper, AuthoritityRepository authorityRepository) {
        this.usersBURepository = usersBURepository;
        this.usersBUMapper = usersBUMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitRepository = businessUnitRepository;
        this.inviteRepository = inviteRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.authoritityRepository = authorityRepository;
    }

    @Transactional
    public List<BusinessUnitAuthoritiesDTO> findAllDistinctCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException {
        try {
            //It's safe to use the id here for it to be more performant
            //Will do if more perf is needed but I doubt it would make a big difference for the life of this
            //This is for every single method here that needs to do something using the authenticated user
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //TODO: This is a n+1 query. Will have to fix in the future
            //TODO: Better name needed for toAuthoritiesDTO method
            List<BusinessUnitAuthoritiesDTO> userBUAuthorities = userBUMapper.toAuthoritiesDTO(
                    userBURepository.findAllByUserEmailAndBusinessUnitType(email, TypeName.COMPANY));

            if(userBUAuthorities.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitAuthorities found");
            }

            return userBUAuthorities;

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    @Transactional
    public void createCompany(CompanyDTO companyDTO) throws UserUnauthenticatedException, FailedToSaveException {
        try {
            //You can get the user id from the authorities in case you need extra perf
            //This is just more readable
            Optional<User> user = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                //Should never be thrown cuz of the login filter the request goes through before coming here
                //but doing it so there isn't an unexpected exception thrown bellow in case something goes wrong
                throw new UserUnauthenticatedException("User is unauthenticated!");
            }

            //Save the bu to the db
            BusinessUnit company = businessUnitMapper.toBusinessUnitEntity(companyDTO);
            businessUnitRepository.save(company);

            //Save the two roles every bu comes with
            //TODO: Remove all the "unsafe" .get without elseThrow. (Pain in the ass to debug later)
            Role adminRole = new Role(null, "Admin",
                    (List<Authority>) authoritityRepository.findAll(),//Get all authorities from the db
                    company);
            Role defaultRole = new Role(null, "Default",
                    List.of(authoritityRepository.findByName("InteractWithWhiteboard").get()),
                    company);
            roleRepository.saveAll(List.of(adminRole, defaultRole));

            UserBusinessUnit userBU = new UserBusinessUnit(null, user.get(), company, List.of(adminRole));
            usersBURepository.save(userBU);

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }

    //Doing it this way so there is no way to cheese the system
    //You can only update the name for now
    //Might implement other stuff in the future
    @Transactional
    public void updateCompany(CompanyDTO companyDTO) throws FailedToUpdateException, EntityNotFoundException {
        try {
            Optional<BusinessUnit> bu = businessUnitRepository.findById(companyDTO.id());

            if(bu.isEmpty()){
                throw new EntityNotFoundException("You can't update a company without id");
            }

            bu.get().setName(companyDTO.name());

            businessUnitRepository.save(bu.get());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    @Transactional
    public void leaveCompany(CompanyDTO companyDTO) throws FailedToLeaveException, FailedToDeleteException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //Delete invite for this user to this company
            //Probably "slow". You can just straight up make a delete method
            inviteRepository.findInviteByBusinessUnitIdAndReceiverEmail(companyDTO.id(), email)
                    .ifPresent(inviteRepository::delete);

            //Delete all userBU entries
            List<UserBusinessUnit> userBUs =
                    userBURepository.findAllByUserEmailAndBusinessUnitId(email, companyDTO.id());

            userBURepository.deleteAll(userBUs);

            //Delete all child userBURole entries
            List<UserBusinessUnit> childUserBUs =
                    userBURepository.findAllByUserEmailAndBusinessUnitCompanyId(email, companyDTO.id());

            //Delete all
            userBURepository.deleteAll(childUserBUs);

            //if no more users are left in the company delete it (and it's children ofc)
            if(userBURepository.countAllByBusinessUnitId(companyDTO.id()) == 0){
                deleteCompany(companyDTO);
            }

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToLeaveException("Failed to leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCompany(CompanyDTO companyDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez upgrade as I don't rely on cascading for deletion

            //Delete all invites for the company
            inviteRepository.deleteAllByBusinessUnitId(companyDTO.id());

            //Delete all roles in the company
            roleRepository.deleteAllByBusinessUnitId(companyDTO.id());

            //Delete userBURoles
            userBURepository.deleteAllByBusinessUnitId(companyDTO.id());

            //helper method for children deletion
            deleteAllProjects(businessUnitRepository.findAllByCompanyId(companyDTO.id()));

            //Finally delete company
            businessUnitRepository.deleteById(companyDTO.id());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<BusinessUnitAuthoritiesDTO> findAllProjectsByAuthenticatedUserAndCompany(CompanyDTO companyDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            String email  = SecurityContextHolder.getContext().getAuthentication().getName();

            List<BusinessUnitAuthoritiesDTO> userBUAuthorities = userBUMapper.toAuthoritiesDTO(
                    userBURepository.findAllByUserEmailAndBusinessUnitCompanyIdAndBusinessUnitType(email, companyDTO.id(), TypeName.PROJECT));

            if(userBUAuthorities.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitAuthorities found");
            }

            return userBUAuthorities;

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO) throws UserUnauthenticatedException, FailedToSaveException {
        try {
            //You can get the user id from the authorities in case you need extra perf
            //This is just more readable
            Optional<User> user = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                //Should never be thrown cuz of the login filter the request goes through before coming here
                //but doing it so there isn't an unexpected exception thrown bellow in case something goes wrong
                throw new UserUnauthenticatedException("User is unauthenticated!");
            }

            //Save the bu to the db
            BusinessUnit project = businessUnitMapper.toBusinessUnitEntity(projectDTO);
            businessUnitRepository.save(project);

            //Save the two roles every bu comes with
            //TODO: Remove all the "unsafe" .get without elseThrow. (Pain in the ass to debug later)
            Role adminRole = new Role(null, "Admin",
                    (List<Authority>) authoritityRepository.findAll(),//Get all authorities from the db
                    project);
            Role defaultRole = new Role(null, "Default",
                    List.of(authoritityRepository.findByName("InteractWithWhiteboard").get()),
                    project);
            roleRepository.saveAll(List.of(adminRole, defaultRole));

            UserBusinessUnit userBU = new UserBusinessUnit(null, user.get(), project, List.of(adminRole));
            usersBURepository.save(userBU);

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }

    //Doing it this way so there is no way to cheese the system
    //You can only update the name for now
    //Might implement other stuff in the future
    @Transactional
    public void updateProject(ProjectDTO projectDTO) throws EntityNotFoundException, FailedToUpdateException {
        try {
            Optional<BusinessUnit> bu = businessUnitRepository.findById(projectDTO.id());

            if(bu.isEmpty()){
                throw new EntityNotFoundException("You can't update a project without id");
            }

            bu.get().setName(projectDTO.name());

            businessUnitRepository.save(bu.get());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    @Transactional
    public void leaveProject(ProjectDTO projectDTO) throws FailedToLeaveException, FailedToDeleteException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //Delete invite for this user to this company
            //Probably slow. You can just straight up make a delete method
            inviteRepository.findInviteByBusinessUnitIdAndReceiverEmail(projectDTO.id(), email)
                    .ifPresent(inviteRepository::delete);

            //Delete all userBURole entries
            List<UserBusinessUnit> userBUs =
                    userBURepository.findAllByUserEmailAndBusinessUnitId(email, projectDTO.id());

            userBURepository.deleteAll(userBUs);

            //Delete all child userBURole entries
            List<UserBusinessUnit> childUserBUs =
                    userBURepository.findAllByUserEmailAndBusinessUnitProjectId(email, projectDTO.id());

            userBURepository.deleteAll(childUserBUs);

            //if no more users are left in the project delete it (and it's children ofc)
            if(userBURepository.countAllByBusinessUnitId(projectDTO.id()) == 0){
                deleteProject(projectDTO);
            }

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToLeaveException("Failed to leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProject(ProjectDTO projectDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez change as I don't rely on cascading for deletion
            //Worth a look in the future

            //Delete all invites for the project
            inviteRepository.deleteAllByBusinessUnitId(projectDTO.id());

            //Delete all roles in the project
            roleRepository.deleteAllByBusinessUnitId(projectDTO.id());

            //Delete userBURoles
            userBURepository.deleteAllByBusinessUnitId(projectDTO.id());

            //Here's a good idea to call children but they are taking only 1 dto. I'll just make a helper method.
            deleteAllTeams(businessUnitRepository.findAllByCompanyId(projectDTO.id()));

            //Finally delete project
            businessUnitRepository.deleteById(projectDTO.id());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public List<BusinessUnitAuthoritiesDTO> findAllTeamsByAuthenticatedUserAndProject(ProjectDTO projectDTO) throws FailedToSelectException, EntityNotFoundException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            List<BusinessUnitAuthoritiesDTO> userBUAuthorities = userBUMapper.toAuthoritiesDTO(
                    userBURepository.findAllByUserEmailAndBusinessUnitProjectId(email, projectDTO.id()));

            if(userBUAuthorities.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBUAuthorities;

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToSelectException("Failed to select! " + e.getMessage());
        }
    }

    @Transactional
    public void createTeam(TeamDTO teamDTO) throws UserUnauthenticatedException, FailedToSaveException {
        try {
            //You can get the user id from the authorities in case you need extra perf
            //This is just more readable
            Optional<User> user = userRepository.findUserByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

            if(user.isEmpty()) {
                //Should never be thrown cuz of the login filter the request goes through before coming here
                //but doing it so there isn't an unexpected exception thrown bellow in case something goes wrong
                throw new UserUnauthenticatedException("User is unauthenticated!");
            }

            //Save the bu to the db
            BusinessUnit team = businessUnitMapper.toBusinessUnitEntity(teamDTO);
            businessUnitRepository.save(team);

            //Save the two roles every bu comes with
            //TODO: Remove all the "unsafe" .get without elseThrow. (Pain in the ass to debug later)
            Role adminRole = new Role(null, "Admin",
                    (List<Authority>) authoritityRepository.findAll(),//Get all authorities from the db
                    team);
            Role defaultRole = new Role(null, "Default",
                    List.of(authoritityRepository.findByName("InteractWithWhiteboard").get()),
                    team);
            roleRepository.saveAll(List.of(adminRole, defaultRole));

            UserBusinessUnit userBU = new UserBusinessUnit(null, user.get(), team, List.of(adminRole, defaultRole));
            usersBURepository.save(userBU);

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToSaveException("Failed to save! " + e.getMessage());
        }
    }

    //Doing it this way so there is no way to cheese the system
    //You can only update the name for now
    //Might implement other stuff in the future
    @Transactional
    public void updateTeam(TeamDTO teamDTO) throws EntityNotFoundException, FailedToUpdateException {
        try {
            Optional<BusinessUnit> bu = businessUnitRepository.findById(teamDTO.id());

            if(bu.isEmpty()){
                throw new EntityNotFoundException("You can't update a company without id");
            }

            bu.get().setName(teamDTO.name());

            businessUnitRepository.save(bu.get());
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToUpdateException("Failed to update! " + e.getMessage());
        }
    }

    @Transactional
    public void leaveTeam(TeamDTO teamDTO) throws FailedToLeaveException, FailedToDeleteException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            //Delete invite for this user to this company
            //Probably slow. You can just straight up make a delete method
            inviteRepository.findInviteByBusinessUnitIdAndReceiverEmail(teamDTO.id(), email)
                    .ifPresent(inviteRepository::delete);

            //Delete all userBU entries
            List<UserBusinessUnit> userBUs =
                    userBURepository.findAllByUserEmailAndBusinessUnitId(email, teamDTO.id());

            userBURepository.deleteAll(userBUs);

            //if no more users are left in the project delete it (and it's children ofc)
            if(userBURepository.countAllByBusinessUnitId(teamDTO.id()) == 0){
                deleteTeam(teamDTO);
            }

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToLeaveException("Failed to leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteTeam(TeamDTO teamDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez change as I don't rely on cascading for deletion
            //But isn't worth the extra effort for this project

            //Delete all invites for the team
            inviteRepository.deleteAllByBusinessUnitId(teamDTO.id());

            //Delete all roles in the team
            roleRepository.deleteAllByBusinessUnitId(teamDTO.id());

            //Delete userBURoles
            userBURepository.deleteAllByBusinessUnitId(teamDTO.id());

            //Finally delete team
            businessUnitRepository.deleteById(teamDTO.id());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    //To put it simply I'm not sure if I'm doing something wrong (probably I am)
    public void deleteAuthoritiesFromSecurityContext(List<UserBusinessUnit> userBUs) throws NoSuchElementException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());

        //No way people prefer this rather than 2 for loops. What hell man
        //Whole point of this monstrosity is to filter out/remove every role that is in userBUs
        //from the current session
        updatedAuthorities.addAll(
                auth.getAuthorities().stream().filter(securityId ->
                                userBUs.stream().noneMatch(childUBUR ->
                                        childUBUR.getId() ==
                                                Integer.parseInt(
                                                        Arrays.stream(securityId.getAuthority().split(":")).findFirst().get())))

                        .toList());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    //TODO: Move this shit to the role service
    //Probably a good idea to modify it to take a list
    public void addAuthoritiesToSecurityContext(UserBusinessUnit userBU){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.addAll(auth.getAuthorities());//add the authorities we already had
        updatedAuthorities.add(new SecurityIds( //Add the new authority
                userBU.getId(),
                userBU.getUser().getId(),
                userBU.getBusinessUnit().getId(),
                userBU.getRoles().stream().mapToLong(Role::getId).boxed().toList()
        ));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }


    //Was supposed to be private but transactional requires the method is public
    //Better make sure this is called from somewhere where a transaction already exists rather than risking it an keeping it private
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAllProjects(List<BusinessUnit> projects) throws FailedToDeleteException {
        try{
            deleteAllUserBUsAndInvites(projects);

            //Delete Roles
            projects.forEach(project -> roleRepository.deleteAllByBusinessUnitId(project.getId()));

            //Probably an idiotic way to do this
            //Delete child Teams
            List<BusinessUnit> businessUnits = new ArrayList<>();
            projects.forEach(
                    project -> businessUnits.addAll(businessUnitRepository.findAllByProjectId(project.getId())));

            deleteAllTeams(businessUnits);

            //Delete all projects
            businessUnitRepository.deleteAll(projects);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //Was supposed to be private but transactional requires the method is public
    //Better make sure this is called from somewhere where a transaction already exists rather than risking it an keeping it private
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAllTeams(List<BusinessUnit> teams) throws FailedToDeleteException {
        try{
            deleteAllUserBUsAndInvites(teams);

            //Delete Roles
            teams.forEach(team -> roleRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete all teams
            businessUnitRepository.deleteAll(teams);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAllUserBUsAndInvites(List<BusinessUnit> businessUnits) throws FailedToDeleteException {
        try{
            //Delete Invites
            businessUnits.forEach(bu -> inviteRepository.deleteAllByBusinessUnitId(bu.getId()));

            //Delete UBURs
            businessUnits.forEach(bu -> usersBURepository.deleteAllByBusinessUnitId(bu.getId()));

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

}

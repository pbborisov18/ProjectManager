package com.company.projectManager.common.service.impl;

import com.company.projectManager.common.dto.*;
import com.company.projectManager.common.dto.businessUnit.CompanyDTO;
import com.company.projectManager.common.dto.businessUnit.ProjectDTO;
import com.company.projectManager.common.dto.businessUnit.TeamDTO;
import com.company.projectManager.common.entity.*;
import com.company.projectManager.common.exception.*;
import com.company.projectManager.common.mapper.BusinessUnitMapper;
import com.company.projectManager.common.mapper.UsersBusinessUnitsMapper;
import com.company.projectManager.common.repository.*;
import com.company.projectManager.common.security.SecurityIds;
import com.company.projectManager.common.service.UsersBusinessUnitsService;
import com.company.projectManager.common.utils.TypeName;
import com.company.projectManager.invitation.repository.InviteRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
public class UsersBusinessUnitsServiceImpl implements UsersBusinessUnitsService {

    private final UsersBusinessUnitsRepository userBURepository;

    private final UsersBusinessUnitsMapper userBUMapper;


    private final UserRepository userRepository;


    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitMapper businessUnitMapper;


    private final InviteRepository inviteRepository;


    private final RoleRepository roleRepository;


    private final AuthoritityRepository authoritityRepository;

    public UsersBusinessUnitsServiceImpl(UsersBusinessUnitsRepository userBURepository, UsersBusinessUnitsMapper userBUMapper, UserRepository userRepository, BusinessUnitMapper businessUnitMapper, BusinessUnitRepository businessUnitRepository, InviteRepository inviteRepository,
                                         RoleRepository roleRepository, AuthoritityRepository authorityRepository) {
        this.userBURepository = userBURepository;
        this.userBUMapper = userBUMapper;
        this.userRepository = userRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitRepository = businessUnitRepository;
        this.inviteRepository = inviteRepository;
        this.roleRepository = roleRepository;
        this.authoritityRepository = authorityRepository;
    }

    @Transactional
    public List<BusinessUnitAuthoritiesDTO> findAllDistinctCompaniesByAuthenticatedUser() throws FailedToSelectException, EntityNotFoundException {
        try {
            //It's safe to use the id here for it to be more performant. You can get it from the authorities, 2nd "id" (Check out the class SecurityIds)
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
            Role adminRole = new Role(null, "Admin",
                    (List<Authority>) authoritityRepository.findAll(),//Get all authorities from the db
                    company);
            //TODO: Remove all the "unsafe" .get without elseThrow cuz the default exceptions are bad
            //Doesn't trigger roll back for some reason even though method is transactional. wtf?
            Role defaultRole = new Role(null, "Default",
                    List.of(authoritityRepository.findByName("InteractWithWhiteboard").get()),
                    company);
            roleRepository.saveAll(List.of(adminRole, defaultRole));

            UserBusinessUnit userBU = new UserBusinessUnit(null, user.get(), company, List.of(adminRole));
            userBURepository.save(userBU);

            //Add the required authorities without needing the user to re-log
            addAuthoritiesToSecurityContext(userBU);

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

            //Remove the required authorities without needing the user to re-log
            deleteAuthoritiesFromSecurityContext(
                    Stream.concat(userBUs.stream(), childUserBUs.stream()).toList());

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

            //Not 100% necessary
            //cuz in authorization I need to get values from the db. We just deleted the them.
            //So they won't get through the authorization
            //MAYBE TODO: Delete/remove the role from the SecurityContext
//            SessionRegistry sessionRegistry = new SessionRegistryImpl();
//
//            sessionRegistry.getAllPrincipals();
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

            //TODO: Remove all the "unsafe" .get without elseThrow cuz the default exceptions are bad
            //(Took me way to long to figure out why)
            //Save the two roles every bu comes with
            Role adminRole = new Role(null, "Admin",
                    (List<Authority>) authoritityRepository.findAll(),//Get all authorities from the db
                    project);
            Role defaultRole = new Role(null, "Default",
                    List.of(authoritityRepository.findByName("InteractWithWhiteboard").get()),
                    project);
            roleRepository.saveAll(List.of(adminRole, defaultRole));

            UserBusinessUnit userBU = new UserBusinessUnit(null, user.get(), project, List.of(adminRole));
            userBURepository.save(userBU);

            //Add the required authorities without needing the user to re-log
            addAuthoritiesToSecurityContext(userBU);

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
                throw new EntityNotFoundException("You can't update a company without id");
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

            //Remove the required authorities without needing the user to re-log
            deleteAuthoritiesFromSecurityContext(
                    Stream.concat(userBUs.stream(), childUserBUs.stream()).toList());

        } catch (ConstraintViolationException | DataAccessException | NoSuchElementException e) {
            throw new FailedToLeaveException("Failed to leave! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProject(ProjectDTO projectDTO) throws FailedToDeleteException {
        try {
            //If these are slow. Look into batch delete. Should be an ez change as I don't rely on cascading for deletion
            //But isn't worth the extra effort for this project

            //Delete all invites for the project
            inviteRepository.deleteAllByBusinessUnitId(projectDTO.id());

            //TODO: think about this more (prob will need a rework)
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
            Role adminRole = new Role(null, "Admin",
                    (List<Authority>) authoritityRepository.findAll(),//Get all authorities from the db
                    team);
            //TODO: Remove all the "unsafe" .get without elseThrow cuz the default exceptions are bad
            Role defaultRole = new Role(null, "Default",
                    //Probably add support for custom roles in the future
                    List.of(authoritityRepository.findByName("InteractWithWhiteboard").get()),
                    team);
            roleRepository.saveAll(List.of(adminRole, defaultRole));

            UserBusinessUnit userBU = new UserBusinessUnit(null, user.get(), team, List.of(adminRole, defaultRole));
            userBURepository.save(userBU);

            //Add the required authorities without needing the user to re-log
            addAuthoritiesToSecurityContext(userBU);

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

            //Remove the required authorities without needing the user to re-log
            deleteAuthoritiesFromSecurityContext(userBUs);

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

            //TODO: think about this more (prob will need a rework)
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
            //Delete Invites
            projects.forEach(project -> inviteRepository.deleteAllByBusinessUnitId(project.getId()));

            //Delete UBURs
            projects.forEach(project -> userBURepository.deleteAllByBusinessUnitId(project.getId()));

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
            //Delete Invites
            teams.forEach(team -> inviteRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete UBURs
            teams.forEach(team -> userBURepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete Roles
            teams.forEach(team -> roleRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete all teams
            businessUnitRepository.deleteAll(teams);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

}

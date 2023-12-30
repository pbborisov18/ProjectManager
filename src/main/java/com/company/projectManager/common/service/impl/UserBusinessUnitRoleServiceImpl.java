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
import com.company.projectManager.common.service.UserBusinessUnitRoleService;
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
public class UserBusinessUnitRoleServiceImpl implements UserBusinessUnitRoleService {

    private final UsersBusinessUnitsRolesRepository userBURoleRepository;

    private final UsersBusinessUnitsMapper userBURoleMapper;


    private final UserRepository userRepository;


    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitMapper businessUnitMapper;


    private final InviteRepository inviteRepository;


    private final RoleRepository roleRepository;


    private final AuthoritityRepository authoritityRepository;

    public UserBusinessUnitRoleServiceImpl(UsersBusinessUnitsRolesRepository userBURoleRepository, UsersBusinessUnitsMapper userBURoleMapper, UserRepository userRepository, BusinessUnitMapper businessUnitMapper, BusinessUnitRepository businessUnitRepository, InviteRepository inviteRepository,
                                           RoleRepository roleRepository, AuthoritityRepository authorityRepository) {
        this.userBURoleRepository = userBURoleRepository;
        this.userBURoleMapper = userBURoleMapper;
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
            List<BusinessUnitAuthoritiesDTO> userBURoles = userBURoleMapper.toAuthoritiesDTO(
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitType(email, TypeName.COMPANY));

            if(userBURoles.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBURoles;

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

            UserBusinessUnit userBUrole = new UserBusinessUnit(null, user.get(), company, List.of(adminRole));
            userBURoleRepository.save(userBUrole);

            //Add the required authorities without needing the user to re-log
            addAuthoritiesToSecurityContext(userBUrole);

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

            //Delete all userBURole entries
            List<UserBusinessUnit> userBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitId(email, companyDTO.id());

            userBURoleRepository.deleteAll(userBURoles);

            //Delete all child userBURole entries
            List<UserBusinessUnit> childUserBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitCompanyId(email, companyDTO.id());

            //Delete all
            userBURoleRepository.deleteAll(childUserBURoles);

            //if no more users are left in the company delete it (and it's children ofc)
            if(userBURoleRepository.countAllByBusinessUnitId(companyDTO.id()) == 0){
                deleteCompany(companyDTO);
            }

            //Remove the required authorities without needing the user to re-log
            deleteAuthoritiesFromSecurityContext(
                    Stream.concat(userBURoles.stream(), childUserBURoles.stream()).toList());

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
            userBURoleRepository.deleteAllByBusinessUnitId(companyDTO.id());

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

            List<BusinessUnitAuthoritiesDTO> userBURoles = userBURoleMapper.toAuthoritiesDTO(
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitCompanyIdAndBusinessUnitType(email, companyDTO.id(), TypeName.PROJECT));

            if(userBURoles.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBURoles;

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

            UserBusinessUnit userBUrole = new UserBusinessUnit(null, user.get(), project, List.of(adminRole));
            userBURoleRepository.save(userBUrole);

            //Add the required authorities without needing the user to re-log
            addAuthoritiesToSecurityContext(userBUrole);

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

            //Delete all userBURole entries
            List<UserBusinessUnit> userBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitId(email, projectDTO.id());

            userBURoleRepository.deleteAll(userBURoles);

            //Delete all child userBURole entries
            List<UserBusinessUnit> childUserBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitProjectId(email, projectDTO.id());

            userBURoleRepository.deleteAll(childUserBURoles);

            //if no more users are left in the project delete it (and it's children ofc)
            if(userBURoleRepository.countAllByBusinessUnitId(projectDTO.id()) == 0){
                deleteProject(projectDTO);
            }

            //Remove the required authorities without needing the user to re-log
            deleteAuthoritiesFromSecurityContext(
                    Stream.concat(userBURoles.stream(), childUserBURoles.stream()).toList());

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
            userBURoleRepository.deleteAllByBusinessUnitId(projectDTO.id());

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

            List<BusinessUnitAuthoritiesDTO> userBURoles = userBURoleMapper.toAuthoritiesDTO(
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitProjectId(email, projectDTO.id()));

            if(userBURoles.isEmpty()){
                throw new EntityNotFoundException("No UserBusinessUnitRoles found");
            }

            return userBURoles;

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

            UserBusinessUnit userBUrole = new UserBusinessUnit(null, user.get(), team, List.of(adminRole, defaultRole));
            userBURoleRepository.save(userBUrole);

            //Add the required authorities without needing the user to re-log
            addAuthoritiesToSecurityContext(userBUrole);

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

            //Delete all userBURole entries
            List<UserBusinessUnit> userBURoles =
                    userBURoleRepository.findAllByUserEmailAndBusinessUnitId(email, teamDTO.id());

            userBURoleRepository.deleteAll(userBURoles);

            //if no more users are left in the project delete it (and it's children ofc)
            if(userBURoleRepository.countAllByBusinessUnitId(teamDTO.id()) == 0){
                deleteTeam(teamDTO);
            }

            //Remove the required authorities without needing the user to re-log
            deleteAuthoritiesFromSecurityContext(userBURoles);

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
            userBURoleRepository.deleteAllByBusinessUnitId(teamDTO.id());

            //Finally delete team
            businessUnitRepository.deleteById(teamDTO.id());

        } catch (ConstraintViolationException | DataAccessException e) {
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    //To put it simply I'm not sure if I'm doing something wrong (probably I am)
    public void deleteAuthoritiesFromSecurityContext(List<UserBusinessUnit> userBURoles) throws NoSuchElementException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());

        //No way people prefer this rather than 2 for loops. What hell man
        //Whole point of this monstrosity is to filter out/remove every role that is in childUserBURoles and userBURoles
        //from the current session
        updatedAuthorities.addAll(
                auth.getAuthorities().stream().filter(securityId ->
                                userBURoles.stream().noneMatch(childUBUR ->
                                        childUBUR.getId() ==
                                                Integer.parseInt(
                                                        Arrays.stream(securityId.getAuthority().split(":")).findFirst().get())))

                        .toList());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void addAuthoritiesToSecurityContext(UserBusinessUnit userBUrole){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.addAll(auth.getAuthorities());//add the authorities we already had
        updatedAuthorities.add(new SecurityIds( //Add the new authority
                userBUrole.getId(),
                userBUrole.getUser().getId(),
                userBUrole.getBusinessUnit().getId(),
                userBUrole.getRoles().stream().mapToLong(Role::getId).boxed().toList()
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
            projects.forEach(project -> userBURoleRepository.deleteAllByBusinessUnitId(project.getId()));

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
            teams.forEach(team -> userBURoleRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete Roles
            teams.forEach(team -> roleRepository.deleteAllByBusinessUnitId(team.getId()));

            //Delete all teams
            businessUnitRepository.deleteAll(teams);

        } catch (ConstraintViolationException | DataAccessException e){
            throw new FailedToDeleteException("Failed to delete! " + e.getMessage());
        }
    }

}

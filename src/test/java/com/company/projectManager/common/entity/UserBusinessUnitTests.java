package com.company.projectManager.common.entity;

import com.company.projectManager.common.utils.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DataJpaTest
public class UserBusinessUnitTests {

    @Autowired
    private TestEntityManager entityManager;

    Authority auth;
    Role role;
    BusinessUnit businessUnit;
    User user;

    //It's before each cuz spring rollbacks after each test
    @BeforeEach
    void setup(){
        businessUnit = new BusinessUnit(null, "Company1", TypeName.COMPANY, null, null, null);
        auth = new Authority(null, "DoSomething");
        role = new Role(null, "Role", List.of(auth), businessUnit);
        user = new User(null, "test@email.com", "12345");

        entityManager.persist(auth);
        entityManager.persist(businessUnit);
        entityManager.persist(role);
        entityManager.persist(user);
    }

    //Test basic functionality
    @Test
    void basicFunctionality(){
        //Given
        UserBusinessUnit userBu = new UserBusinessUnit(null, user, businessUnit, List.of(role));

        //When
        Long id = entityManager.persistAndGetId(userBu, Long.class);
        UserBusinessUnit foundUserBu = entityManager.find(UserBusinessUnit.class, id);

        //Then
        assertThat(userBu).isEqualTo(foundUserBu);
    }

    //Test unique constraint
    @Test
    void checkUniqueConstraint(){
        //Given
        //(same user and bu)
        UserBusinessUnit userBu1 = new UserBusinessUnit(null, user, businessUnit, List.of(role));
        UserBusinessUnit userBu2 = new UserBusinessUnit(null, user, businessUnit, List.of(role));

        try {
            //When
            entityManager.persist(userBu1);
            entityManager.persist(userBu2);

            //Then
            fail("Unique constraint didn't catch the same name");
        } catch (org.hibernate.exception.ConstraintViolationException e){
            //No fucking idea how to assert if the correct thing is caught
            //Only thing I can check is the name of the unique constraint but thing is
            //They have a randomly generated name each time
            //I guess I'm only checking if the test throws the exception
        }
    }

}

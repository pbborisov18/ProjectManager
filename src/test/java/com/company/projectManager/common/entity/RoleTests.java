package com.company.projectManager.common.entity;

import com.company.projectManager.common.utils.TypeName;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DataJpaTest
public class RoleTests {

    @Autowired
    private TestEntityManager entityManager;

    Authority auth;
    BusinessUnit businessUnit;

    //It's before each cuz spring rollbacks after each test
    @BeforeEach
    void setup(){
        auth = new Authority(null, "DoSomething");
        businessUnit = new BusinessUnit(null, "Company1", TypeName.COMPANY, null, null, null);

        entityManager.persist(auth);
        entityManager.persist(businessUnit);
    }

    //Test basic functionality
    @Test
    void basicFunctionality(){
        //Given
        Role role = new Role(null, "Role", List.of(auth), businessUnit);

        //When
        Long id = entityManager.persistAndGetId(role, Long.class);
        Role foundRole = entityManager.find(Role.class, id);

        //Then
        assertThat(role).isEqualTo(foundRole);
    }

    //Test unique constraint
    @Test
    void checkUniqueConstraint(){
        //Given
        //Same name and businessUnit
        Role role1 = new Role(null, "Role", List.of(auth), businessUnit);
        Role role2 = new Role(null, "Role", List.of(auth), businessUnit);

        try {
            //When
            entityManager.persist(role1);
            entityManager.persist(role2);

            //Then
            fail("Unique constraint didn't catch the same name");
        } catch (org.hibernate.exception.ConstraintViolationException e){
            //No fucking idea how to assert if the correct exception is thrown
            //Only thing I can check is the name of the unique constraint but thing is
            //They have a randomly generated name each time
            //I guess I'm only checking if the test throws the exception
        }
    }

    //Test validations
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " "})
    void checkNameValidations(String name){
        //Given
        Role role = new Role(null, name, List.of(auth), businessUnit);

        try {
            //When
            entityManager.persist(role);

            //Then
            fail("This didn't throw ConstraintViolationException but it should");
        } catch (ConstraintViolationException e){
            //Why am I doing this?
            //Cuz it's pain in the ass to compare lists
            //This is basically checking if either 1 or both constraints were thrown
            assertThat(e.getConstraintViolations().size()).satisfiesAnyOf(
                    constraintSize -> assertThat(constraintSize).isEqualTo(1),
                    constraintSize -> assertThat(constraintSize).isEqualTo(2)
            );
        }
    }

    @Test
    void checkAuthoritiesValidations(){
        //Given
        Role role = new Role(null, "Role", null, businessUnit);

        try {
            //When
            entityManager.persist(role);

            //Then
            fail("This didn't throw ConstraintViolationException but it should");
        } catch (ConstraintViolationException e){
            assertThat(e.getConstraintViolations().size()).isEqualTo(1);
        }
    }

    @Test
    void checkBusinessUnitValidations(){
        //Given
        Role role = new Role(null, "Role", List.of(auth), null);

        try {
            //When
            entityManager.persist(role);

            //Then
            fail("This didn't throw ConstraintViolationException but it should");
        } catch (ConstraintViolationException e){
            assertThat(e.getConstraintViolations().size()).isEqualTo(1);
        }
    }

}

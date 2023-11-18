package com.company.projectManager.common.entity;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DataJpaTest
public class AuthorityTests {

    @Autowired
    private TestEntityManager entityManager;

    //Test basic functionality
    @Test
    void basicFunctionality(){
        //Given
        Authority authority = new Authority(null, "DoSomething");

        //When
        Long id = entityManager.persistAndGetId(authority, Long.class);
        Authority foundAuthority = entityManager.find(Authority.class, id);

        //Then
        assertThat(authority).isEqualTo(foundAuthority);
    }


    //Test the unique constraint
    @Test
    void checkNameUniqueness(){
        //Given
        Authority authority1 = new Authority(null, "DoSomething");
        Authority authority2 = new Authority(null, "DoSomething");

        try {
            //When
            entityManager.persist(authority1);
            entityManager.persist(authority2);

            //Then
            fail("Unique constraint didn't catch the same name");
        } catch (org.hibernate.exception.ConstraintViolationException e){
            //No fucking idea how to assert if the correct exception is thrown
            //Only thing I can check is the name of the unique constraint but thing is
            //They have a randomly generated name each time
            //I guess I'm only checking if the test throws the exception
        }
    }

    //Test name validations
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " "})
    void checkNameValidations(String name){
        //Given
        Authority authority = new Authority(null, name);

        try {
            //When
            entityManager.persist(authority);

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
}

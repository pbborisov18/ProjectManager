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
public class UserTests {

    @Autowired
    private TestEntityManager entityManager;

    //Basic functionality I guess
    //If this doesn't work I guess something is very wrong
    @Test
    void checkEmailValidity(){
        //Given
        User user = new User(null, "valid@email.com", "12345");

        //When
        Long id = entityManager.persistAndGetId(user, Long.class);
        User foundUser = entityManager.find(User.class, id);

        //Then
        assertThat(user).isEqualTo(foundUser);
    }

    //Check the unique constraint for the email
    @Test
    void checkEmailUniqueness() {
        //Given
        User user1 = new User(null, "same@email.com", "12345");
        User user2 = new User(null, "same@email.com", "12345");

        try {
            //When
            entityManager.persist(user1);
            entityManager.persist(user2);

            //Then
            fail("Unique constraint didn't catch the same email");
        } catch (org.hibernate.exception.ConstraintViolationException e){
            //No fucking idea how to assert if the correct exception is thrown
            //Only thing I can check is the name of the unique constraint but thing is
            //They have a randomly generated name each time
            //I guess I'm only checking if the test throws the exception
        }
    }

    //Check if the validations are working on the email
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "invalidemail.com"})
    void checkEmailValidations(String email){
        //Given
        User user = new User(null, email, "12345");

        try {
            //When
            entityManager.persist(user);

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

    //Check if the validations are working on the password
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " "})
    void checkPasswordValidations(String password){
        //Given
        User user = new User(null, "valid@email.com", password);

        try {
            //When
            entityManager.persist(user);

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

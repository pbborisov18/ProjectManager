package com.company.projectManager.common.entity;

import com.company.projectManager.common.utils.TypeName;
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
public class BusinessUnitTests {

    @Autowired
    private TestEntityManager entityManager;

    //Test basic functionality
    @Test
    void basicFunctionality(){
        //Given
        BusinessUnit bu = new BusinessUnit(null, "BU", TypeName.COMPANY, null, null, null);

        //When
        Long id = entityManager.persistAndGetId(bu, Long.class);
        BusinessUnit foundBu = entityManager.find(BusinessUnit.class, id);

        //Then
        assertThat(bu).isEqualTo(foundBu);
    }


    //Test validations
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " "})
    void checkNameValidations(String name){
        //Given
        BusinessUnit bu = new BusinessUnit(null, name, TypeName.COMPANY, null, null, null);

        try {
            //When
            entityManager.persist(bu);

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
    void checkTypeValidations(){
        //Given
        BusinessUnit bu = new BusinessUnit(null, "bu", null, null, null, null);

        try {
            //When
            entityManager.persist(bu);

            //Then
            fail("This didn't throw ConstraintViolationException but it should");
        } catch (ConstraintViolationException e){
            //Why am I doing this?
            //Cuz it's pain in the ass to compare lists
            //This is basically checking if either 1 or both constraints were thrown
            assertThat(e.getConstraintViolations().size()).isEqualTo(1);
        }
    }

    //Tests can also be written about the relationships and cascading
    //Butttt.... at this point you are testing the framework. You don't use a framework you don't trust
}

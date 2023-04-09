package com.company.projectManager.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UserWithoutPasswordDTO(
        //the get dto shouldn't have any validators because it will only be used out of the db
        //and to save to the db you have to go through the post dto's validations
        //will leave it here cuz some idiot(me) might decide to do something stupid later on
        //if you need a little bit more performance you can remove all the validator annotations
        Long id,

        @Email
        @NotNull
        @NotBlank
        String email
) {

}

package com.company.projectManager.controllers;

import com.company.projectManager.DTOs.UserWithoutPasswordDTO;
import com.company.projectManager.exceptions.EntityNotFoundException;
import com.company.projectManager.exceptions.FailedToSelectException;
import com.company.projectManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/authenticatedUser")
    public ResponseEntity<Object> getAuthenticatedUser(){
        try {
            UserWithoutPasswordDTO user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (FailedToSelectException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

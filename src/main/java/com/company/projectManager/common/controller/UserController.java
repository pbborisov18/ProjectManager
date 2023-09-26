package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.UserDTO;
import com.company.projectManager.common.dto.UserWithoutPasswordDTO;
import com.company.projectManager.common.exception.EntityNotFoundException;
import com.company.projectManager.common.exception.FailedToSaveException;
import com.company.projectManager.common.exception.FailedToSelectException;
import com.company.projectManager.common.exception.UserAlreadyExistsException;
import com.company.projectManager.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> requestBody) {

        if(requestBody.get("password").equals(requestBody.get("confirmPassword"))) {
            try {
                userService.register(new UserDTO(
                        null, requestBody.get("email"), requestBody.get("password")));
            } catch (UserAlreadyExistsException e){
                return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (FailedToSaveException e) {
                return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Passwords aren't matching!", HttpStatus.BAD_REQUEST);
        }

    }
}

package com.company.projectManager.controllers;

import com.company.projectManager.DTOs.UserDTO;
import com.company.projectManager.exceptions.FailedToSaveException;
import com.company.projectManager.exceptions.UserAlreadyExistsException;
import com.company.projectManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;

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

package com.company.projectManager.common.controller;

import com.company.projectManager.common.dto.user.UserDTO;
import com.company.projectManager.common.exception.FailedToSaveException;
import com.company.projectManager.common.exception.UserAlreadyExistsException;
import com.company.projectManager.common.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: Email verification
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> requestBody) {

        if(!requestBody.get("password").equals(requestBody.get("confirmPassword"))) {
            return new ResponseEntity<>("Passwords aren't matching!", HttpStatus.BAD_REQUEST);
        }

        try {
            userService.register(new UserDTO(
                    null, requestBody.get("email"), requestBody.get("password")));
        } catch (UserAlreadyExistsException e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (FailedToSaveException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO: Change email
    //TODO: Change password
    //TODO: Delete account
    //TODO: Forgot password
}

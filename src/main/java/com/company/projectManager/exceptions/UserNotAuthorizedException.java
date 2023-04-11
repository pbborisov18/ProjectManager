package com.company.projectManager.exceptions;

public class UserNotAuthorizedException extends Exception {

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}

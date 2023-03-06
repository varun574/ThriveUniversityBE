package com.example.springboot.thriveuniversitybackend.Public.exceptions;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException(String s) {
        super(s);
    }
}

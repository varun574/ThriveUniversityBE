package com.example.springboot.thriveuniversitybackend.Public.exceptions;

public class UserAlreadyLoggedInException extends RuntimeException{
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}

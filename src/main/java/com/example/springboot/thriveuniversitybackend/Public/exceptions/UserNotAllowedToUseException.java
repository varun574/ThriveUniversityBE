package com.example.springboot.thriveuniversitybackend.Public.exceptions;

public class UserNotAllowedToUseException extends RuntimeException{
    public UserNotAllowedToUseException(String message) {
        super(message);
    }
}

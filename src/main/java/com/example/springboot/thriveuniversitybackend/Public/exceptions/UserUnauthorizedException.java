package com.example.springboot.thriveuniversitybackend.Public.exceptions;

public class UserUnauthorizedException extends RuntimeException{

    public UserUnauthorizedException(String message) {
        super(message);
    }
}

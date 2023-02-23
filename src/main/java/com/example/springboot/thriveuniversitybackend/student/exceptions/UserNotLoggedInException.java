package com.example.springboot.thriveuniversitybackend.student.exceptions;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException(String s) {
        super(s);
    }
}

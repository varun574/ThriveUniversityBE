package com.example.springboot.thriveuniversitybackend.student.exceptions;

public class OldPasswardDoNotMatchException extends RuntimeException{
    public OldPasswardDoNotMatchException(String message) {
        super(message);
    }
}

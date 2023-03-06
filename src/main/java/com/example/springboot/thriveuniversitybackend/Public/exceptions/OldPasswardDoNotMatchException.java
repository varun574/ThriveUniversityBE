package com.example.springboot.thriveuniversitybackend.Public.exceptions;

public class OldPasswardDoNotMatchException extends RuntimeException{
    public OldPasswardDoNotMatchException(String message) {
        super(message);
    }
}

package com.example.springboot.thriveuniversitybackend.student.exceptions;

public class RollNumberDoesNotExistException extends RuntimeException{
    public RollNumberDoesNotExistException(String message) {
        super(message);
    }
}

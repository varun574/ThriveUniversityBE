package com.example.springboot.thriveuniversitybackend.task.exceptions;

public class AssignmentNotFoundException extends RuntimeException{
    public AssignmentNotFoundException(String message) {
        super(message);
    }
}

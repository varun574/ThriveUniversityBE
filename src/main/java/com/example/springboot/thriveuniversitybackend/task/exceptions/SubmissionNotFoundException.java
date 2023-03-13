package com.example.springboot.thriveuniversitybackend.task.exceptions;

public class SubmissionNotFoundException extends RuntimeException{
    public SubmissionNotFoundException(String message) {
        super(message);
    }
}

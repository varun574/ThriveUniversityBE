package com.example.springboot.thriveuniversitybackend.admin.exceptions;

public class AdmissionNotFoundException extends RuntimeException{
    public AdmissionNotFoundException(String message) {
        super(message);
    }
}

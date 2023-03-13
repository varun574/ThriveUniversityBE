package com.example.springboot.thriveuniversitybackend.Public.exceptions;

public class UniqueConstraintException extends RuntimeException{
    public String fieldName;
    public UniqueConstraintException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
}

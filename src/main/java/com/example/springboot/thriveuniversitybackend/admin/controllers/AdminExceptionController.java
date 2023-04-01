package com.example.springboot.thriveuniversitybackend.admin.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.ErrorResponseDto;
import com.example.springboot.thriveuniversitybackend.admin.exceptions.AdmissionNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class AdminExceptionController {
    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<ErrorResponseDto> exception(DuplicateKeyException exception){
        ErrorResponseDto response = new ErrorResponseDto("Value already exists", HttpStatus.BAD_REQUEST.value(), new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AdmissionNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> exception(AdmissionNotFoundException exception){
        ErrorResponseDto response = new ErrorResponseDto("Admission not found", HttpStatus.NOT_FOUND.value(), new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

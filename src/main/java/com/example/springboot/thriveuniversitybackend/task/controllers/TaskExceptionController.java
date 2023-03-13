package com.example.springboot.thriveuniversitybackend.task.controllers;

import com.example.springboot.thriveuniversitybackend.Public.dtos.ErrorResponseDto;
import com.example.springboot.thriveuniversitybackend.task.exceptions.AssignmentNotFoundException;
import com.example.springboot.thriveuniversitybackend.task.exceptions.SubmissionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class TaskExceptionController {
    @ExceptionHandler({AssignmentNotFoundException.class, SubmissionNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> exception(Exception exception){
        ErrorResponseDto response = new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND.value(), new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

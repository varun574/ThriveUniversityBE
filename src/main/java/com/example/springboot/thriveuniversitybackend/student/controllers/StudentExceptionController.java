package com.example.springboot.thriveuniversitybackend.student.controllers;

import com.example.springboot.thriveuniversitybackend.student.dtos.ErrorResponse;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotLoggedInException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


@ControllerAdvice
public class StudentExceptionController {

    @ExceptionHandler({UserNotLoggedInException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> exception(RuntimeException exception, HttpSession session){
        session.invalidate();
        ErrorResponse response = new ErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), new HashMap<>());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> constraintException(ConstraintViolationException exception, HttpSession session){
        HashMap<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        for (ConstraintViolation violation: violations
             ) {
            Iterator iterator = violation.getPropertyPath().iterator();
            String leafProperty = "";
            while(iterator.hasNext()){
                leafProperty = iterator.next().toString();
            }
            errors.put(leafProperty, violation.getMessage());
        }
        ErrorResponse response = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }
}

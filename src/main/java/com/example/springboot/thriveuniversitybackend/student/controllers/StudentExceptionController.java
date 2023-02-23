package com.example.springboot.thriveuniversitybackend.student.controllers;

import com.example.springboot.thriveuniversitybackend.student.dtos.ErrorResponse;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotFoundException;
import com.example.springboot.thriveuniversitybackend.student.exceptions.UserNotLoggedInException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class StudentExceptionController {

    @ExceptionHandler({UserNotLoggedInException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> exception(RuntimeException exception, HttpSession session){
        session.invalidate();
        ErrorResponse response = new ErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.UNAUTHORIZED);
    }
}

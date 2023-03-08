package com.example.springboot.thriveuniversitybackend.Public.controllers;

import com.example.springboot.thriveuniversitybackend.Public.exceptions.*;
import com.example.springboot.thriveuniversitybackend.otp.OTPExpiredException;
import com.example.springboot.thriveuniversitybackend.otp.OTPMismatchException;
import com.example.springboot.thriveuniversitybackend.Public.dtos.ErrorResponseDto;
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
public class PublicExceptionController {
    @ExceptionHandler({UserNotLoggedInException.class, UserNotFoundException.class, OTPExpiredException.class, OTPMismatchException.class, UserNotLoggedInException.class})
    public ResponseEntity<ErrorResponseDto> exception(RuntimeException exception, HttpSession session){
        session.invalidate();
        ErrorResponseDto response = new ErrorResponseDto(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UserAlreadyLoggedInException.class, OldPasswardDoNotMatchException.class})
    public ResponseEntity<ErrorResponseDto> exception(RuntimeException exception){
        ErrorResponseDto response = new ErrorResponseDto(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UniqueConstraintException.class})
    public ResponseEntity<ErrorResponseDto> exception(UniqueConstraintException exception){
        HashMap<String , String> errors = new HashMap<>();
        errors.put(exception.fieldName, exception.fieldName+" already exists");
        ErrorResponseDto response = new ErrorResponseDto(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponseDto> constraintException(ConstraintViolationException exception, HttpSession session){
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
        ErrorResponseDto response = new ErrorResponseDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.example.springboot.thriveuniversitybackend.student.controllers;
import com.example.springboot.thriveuniversitybackend.Public.dtos.ErrorResponseDto;
import com.example.springboot.thriveuniversitybackend.student.exceptions.RollNumberDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StudentExceptionController {
    @ExceptionHandler({RollNumberDoesNotExistException.class})
    public ResponseEntity<ErrorResponseDto> exception(RollNumberDoesNotExistException exception){
        ErrorResponseDto response = new ErrorResponseDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.example.springboot.thriveuniversitybackend.student.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ErrorResponse {
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.timestamp = new Date();
        this.status = status;
    }
}

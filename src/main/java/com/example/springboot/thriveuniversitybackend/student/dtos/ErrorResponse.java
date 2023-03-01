package com.example.springboot.thriveuniversitybackend.student.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorResponse {
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int status;
    private Map<String, String> errors;

    public ErrorResponse(String message, int status, HashMap<String, String> errors) {
        this.message = message;
        this.timestamp = new Date();
        this.status = status;
        this.errors = errors;
    }
}

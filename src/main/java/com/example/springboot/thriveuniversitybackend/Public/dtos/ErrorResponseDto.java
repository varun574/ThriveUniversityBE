package com.example.springboot.thriveuniversitybackend.Public.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorResponseDto {
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int status;
    private Map<String, String> errors;

    public ErrorResponseDto(String message, int status, HashMap<String, String> errors) {
        this.message = message;
        this.timestamp = new Date();
        this.status = status;
        this.errors = errors;
    }
}

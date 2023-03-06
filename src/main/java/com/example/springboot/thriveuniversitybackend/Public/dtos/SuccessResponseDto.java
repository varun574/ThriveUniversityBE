package com.example.springboot.thriveuniversitybackend.Public.dtos;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SuccessResponseDto<T> {
    private String message;
    private T data;

    public SuccessResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}

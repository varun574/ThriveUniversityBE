package com.example.springboot.thriveuniversitybackend.Public.dtos;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String email;
    private String name;
    private String type;
}

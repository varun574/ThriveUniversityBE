package com.example.springboot.thriveuniversitybackend.Public.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeStatusDto {
    private String company;
    private String start_date;
    private String description;
    private String end_date;
    private String designation;
}

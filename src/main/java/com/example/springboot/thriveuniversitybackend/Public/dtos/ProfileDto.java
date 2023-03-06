package com.example.springboot.thriveuniversitybackend.Public.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDto {
    private String rollNo;
    private String fatherName;
    private String motherName;
    private LocalDate dob;
    private String personalEmail;
    private String mobileNumber;
    private String department;
    private String section;
    private String educationLevel;
    private String address;
}

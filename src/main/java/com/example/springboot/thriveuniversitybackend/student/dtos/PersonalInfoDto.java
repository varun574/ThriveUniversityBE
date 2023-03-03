package com.example.springboot.thriveuniversitybackend.student.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalInfoDto {
    private String fatherName;
    private String motherName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String personalEmail;
    private String mobileNumber;
    private String department;
    private String section;
    private String educationLevel;
    private String address;
}


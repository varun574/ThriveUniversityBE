package com.example.springboot.thriveuniversitybackend.Public.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdmissionDto {
    private String full_name;
    private String father_name;
    private String mother_name;
    private String mobile_number;
    private LocalDate date_of_birth;
    private String email;
    private String department;
    private String course;
    private String address;
    private String level_of_education;
    private EmployeeStatusDto[] employment_status;
    private EducationDto[] education;
    private ExamDetailsDto[] exam_details;

}


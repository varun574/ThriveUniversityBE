package com.example.springboot.thriveuniversitybackend.student.dtos;

import com.example.springboot.thriveuniversitybackend.student.models.AcademicYear;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StudentDto {
    private String rollNo;
    private AcademicYear academicYear;
    private String fatherName;
    private String motherName;
    private LocalDate dob;
    private String personalEmail;
    private String mobileNumber;
    private String department;
    private String section;
    private String educationLevel;
    private String address;
    private String profilePictureURL;
    private List<String> certificates;
}

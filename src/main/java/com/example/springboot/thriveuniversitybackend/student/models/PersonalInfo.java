package com.example.springboot.thriveuniversitybackend.student.models;

import com.example.springboot.thriveuniversitybackend.enums.Department;
import com.example.springboot.thriveuniversitybackend.enums.EducationLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("personalInfo")
public class PersonalInfo {
    private String fatherName;
    private String motherName;
    private Date dob;
    private String mobileNumber;
    @Enumerated(EnumType.STRING)
    private Department department;
    private String section;
    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;
    private String address;
}

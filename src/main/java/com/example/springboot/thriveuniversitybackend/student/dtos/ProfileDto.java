package com.example.springboot.thriveuniversitybackend.student.dtos;

import com.example.springboot.thriveuniversitybackend.student.models.PersonalInfo;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDto {
    private String email;
    private String name;
    private String rollNo;
    private PersonalInfo personalInfo;
    private String profilePictureURL;
    private List<String> certificates;
}

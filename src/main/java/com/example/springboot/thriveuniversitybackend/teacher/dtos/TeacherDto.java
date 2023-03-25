package com.example.springboot.thriveuniversitybackend.teacher.dtos;


import lombok.Data;

@Data
public class TeacherDto {
    private String name;
    private String email;
    private String mobileNumber;
    private String personalEmail;
    private String department;
    private String address;
    private String profilePictureURL;
}

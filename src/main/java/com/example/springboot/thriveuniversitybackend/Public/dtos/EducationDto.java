package com.example.springboot.thriveuniversitybackend.Public.dtos;

import com.example.springboot.thriveuniversitybackend.attachment.Attachment;
import lombok.Data;

@Data
public class EducationDto {
    private String college_name;
    private String gpa;
    private String level_of_education;
    private Attachment file;
}

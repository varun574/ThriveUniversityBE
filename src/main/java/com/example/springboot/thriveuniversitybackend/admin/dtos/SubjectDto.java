package com.example.springboot.thriveuniversitybackend.admin.dtos;

import com.example.springboot.thriveuniversitybackend.enums.Department;
import lombok.Data;

@Data
public class SubjectDto {
    private String id;
    private String subjectName;
    private Department department;
}
